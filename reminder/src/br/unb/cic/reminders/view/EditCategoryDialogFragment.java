package br.unb.cic.reminders.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.controller.Controller;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.InvalidTextException;
import br.unb.cic.reminders2.R;

public class EditCategoryDialogFragment extends DialogFragment {

	// The category that is being edited
	Category category;

	public EditCategoryDialogFragment(Category category) {
		this.category = category;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Add one title to the dialog
		builder.setTitle(R.string.dialog_editcategory_title);
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		final View view = inflater.inflate(R.layout.category_dialog, null);

		// Initiates the editText with the preview category information:
		EditText edtCategoryName = (EditText) view.findViewById(R.id.dialog_category);
		edtCategoryName.setText(category.getName());

		builder.setView(view)
				// Add action buttons
				.setPositiveButton(R.string.dialog_editcategory_save, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Add the category informed in the edit text
						// Gets the EditText, then its content and updates the
						// category with its content
						EditText edtCategoryName = (EditText) view.findViewById(R.id.dialog_category);
						try {
							category.setName(edtCategoryName.getText().toString());
							Controller.instance(getActivity()).updateCategory(category);
						} catch (InvalidTextException e) {
							Log.e("CategoryDialogFragment", e.getMessage()); // TODO:Handle
																				// it
																				// properly.
							e.printStackTrace();
							Toast.makeText(getActivity().getApplicationContext(), "Categoria inválida.",
									Toast.LENGTH_SHORT).show();
						} catch (DBException e) {
							Log.e("CategoryDialogFragment", e.getMessage()); // TODO:Handle
																				// it
																				// properly.
							e.printStackTrace();
						} catch (Exception e) {
							Log.e("CategoryDialogFragment", e.getMessage()); // TODO:
																				// Handle
																				// it
																				// properly.
							e.printStackTrace();
						}
					}
				})
				// Cancel the dialog
				.setNegativeButton(R.string.dialog_category_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						EditCategoryDialogFragment.this.getDialog().cancel();
					}
				});

		return builder.create();
	}

	/**
	 * Recreates the preview activity to update the categories list.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().recreate();
	}
}
