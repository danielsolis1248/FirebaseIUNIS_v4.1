package com.example.firebaseiunis_v41

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.firebaseiunis_v41.databinding.FragmentDialogAddBinding

class AddDialogFragment: DialogFragment(), DialogInterface.OnShowListener {
    private var binding: FragmentDialogAddBinding? = null

    private var positiveButton:Button? = null
    private var negativeButton:Button? = null

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

        dialog?.let {
            positiveButton = it.getButton(Dialog.BUTTON_POSITIVE)
            negativeButton = it.getButton(Dialog.BUTTON_NEGATIVE)

            positiveButton?.setOnClickListener {
                binding?.let {
                    val product = Product(name = it.teName.text.toString(),
                        description = it.teDescription.text.toString(),
                        quantity = it.teQuantity.text.toString(),
                        precio = it.tePrecio.text.toString().toDouble()
                    )
                }
            }

            negativeButton?.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun save(product: Product){
        TODO()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}