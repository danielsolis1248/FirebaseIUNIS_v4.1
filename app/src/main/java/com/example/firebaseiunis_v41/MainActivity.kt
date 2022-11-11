package com.example.firebaseiunis_v40

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebaseiunis_v41.*
import com.example.firebaseiunis_v41.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MainActivity : AppCompatActivity(),OnProductListener, MainAux {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private lateinit var adapter: ProductAdapter

    private lateinit var firestoreListener: ListenerRegistration
    private var productSelected:Product? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        val response = IdpResponse.fromResultIntent(it.data)

            if (it.resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                if (user != null) {
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                } else {

                    if (response == null) {
                        Toast.makeText(this,"Adios...", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configuAuth()
        configRecyclerView()
    }

    private fun configRecyclerView() {
        adapter = ProductAdapter(mutableListOf(),this)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity,3,GridLayoutManager.HORIZONTAL,false)

        }
        (1..20).forEach{
            val product = Product(it.toString(),"Producto $it","Este producto es el $it", "",it,it*1.1)
            adapter.add(product)
        }
    }

    private fun configuAuth() {

        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null) {
                supportActionBar?.title = auth.currentUser?.displayName
                binding.tvInit.visibility = View.VISIBLE
                binding.llProgress.visibility = View.VISIBLE
                binding.nsvProducts.visibility = View.VISIBLE

            } else {
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())
                resultLauncher.launch(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)
        configFirestoreRealtime()
    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)
        firestoreListener.remove()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                AuthUI.getInstance().signOut(this)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Sesion Terminada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            binding.tvInit.visibility = View.GONE
                            binding.llProgress.visibility = View.GONE
                            binding.nsvProducts.visibility = View.GONE
                        } else {
                            Toast.makeText(this, "No se pudo terminar", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(product: Product) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(product: Product) {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection(KeysCommons.COLLECTION_DB_PRODUCTS)
        product.id?.let { id->
            productRef.document(id)
                .delete()
                .addOnFailureListener {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun configButtons(){
        binding.efab.setOnClickListener {
            AddDialogFragment().show(supportFragmentManager, AddDialogFragment::class.java.simpleName)
        }
    }

    private fun configFirestore(){
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .get()
            .addOnSuccessListener { snapshots ->
                for (document in snapshots) {
                    val product = document.toObject(Product::class.java)
                    adapter.add(product)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Error al consultar datos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun configFirestoreRealtime() {
        val db = FirebaseFirestore.getInstance()
        val productRef = db.collection("products")

        firestoreListener = productRef.addSnapshotListener { snapshots, error ->
            if (error!=null){
                Toast.makeText(this, "Error al consultar datos", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            for (snapshots in snapshots!!.documentChanges){
                val product = snapshots.document.toObject(Product::class.java)
                product.id = snapshots.document.id
                when(snapshots.type){
                    DocumentChange.Type.ADDED ->adapter.add(product)
                    DocumentChange.Type.MODIFIED ->adapter.update(product)
                    DocumentChange.Type.REMOVED ->adapter.delete(product)
                }
            }
        }
    }

    override fun getProductSelected(): Product? = productSelected

}