package com.example.firebaseiunis_v41

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.example.firebaseiunis_v41.databinding.FragmentDialogAddBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddDialogFragment: DialogFragment(), DialogInterface.OnShowListener {
    private var binding: FragmentDialogAddBinding? = null

    private var positiveButton:Button? = null
    private var negativeButton:Button? = null
    private var product: Product? = null
    private var photoSelectUri:Uri? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            binding = FragmentDialogAddBinding.inflate(LayoutInflater.from(context))

            binding?.let {
                val builder = AlertDialog.Builder(activity)
                    .setTitle("Agregar producto")
                    .setPositiveButton("Agregar", null)
                    .setNegativeButton("Cancelar", null)
                    .setView(it.root)

                val dialog = builder.create()
                dialog.setOnShowListener(this)

                return dialog
            }
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onShow(p0: DialogInterface?) {
        val dialog = dialog as? AlertDialog

        initProducto()

        dialog?.let {
            positiveButton = it.getButton(Dialog.BUTTON_POSITIVE)
            negativeButton = it.getButton(Dialog.BUTTON_NEGATIVE)

            positiveButton?.setOnClickListener {
                binding?.let {

                    if (product == null) {
                        val product = Product(name = it.teName.text.toString(),
                            description = it.teDescription.text.toString(),
                            quantity = it.teQuantity.text.toString(),
                            precio = it.tePrecio.text.toString().toDouble()
                            save(product) )

                    } else {
                        product?.apply {
                            name = it.teName.text.toString().trim()
                            description = it.teDescription.text.toString().trim()
                            quantity = it.teQuantity.text.toString().toInt()
                            precio = it.tePrecio.text.toString().toDouble()

                        }
                    }


                }
            }

            negativeButton?.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun initProducto() {
        product = (activity as? MainAux)?.getProductSelected()
        product?.let {product ->
            binding?.let {
                it.teName.setText(product.name)
                it.teDescription.setText(product.description)
                it.teQuantity
            }

        }
    }

    private fun save(product: Product){
        val db = FirebaseFirestore.getInstance()
        db.collection(KeysCommons.COLLECTION_DB_PRODUCTS)
            .add(product)
            .addOnSuccessListener {
                Toast.makeText(activity, "Producto agregado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Error al insertar", Toast.LENGTH_SHORT).show()
            }

    }

    private fun update(product: Product) {
        val db = FirebaseFirestore.getInstance()
        product.id?.let { id ->
            db.collection(KeysCommons.COLLECTION_DB_PRODUCTS)
                .document(id)
                .set(product)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Producto actualizado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    enableUI(true)
                    dismiss()
                }

                }

        }
    }

    private fun enableUI(enable:Boolean) {
        positiveButton?.isEnabled = enable
        negativeButton?.isEnabled = enable

        binding?.let {
            with(it){
                enable
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}