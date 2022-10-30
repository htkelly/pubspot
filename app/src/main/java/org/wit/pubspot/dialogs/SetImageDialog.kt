package org.wit.pubspot.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.wit.pubspot.R

class SetImageDialog : DialogFragment() {
    internal lateinit var listener: SetImageDialogListener

    interface SetImageDialogListener {
        fun onSelectChooseImage(dialog: DialogFragment)
        fun onSelectTakePhoto(dialog: DialogFragment)
    }

    override  fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as SetImageDialogListener
        } catch (e: java.lang.ClassCastException) {
            throw ClassCastException((context.toString() + "does not implement SetImageDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.dialog_set_image)
                .setCancelable(true)
                .setItems(R.array.image_choices,
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0) listener.onSelectChooseImage(this)
                        if (which == 1) listener.onSelectTakePhoto(this)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}