package br.unb.cic.reminders.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders.model.InvalidTextException;
import br.unb.cic.reminders2.R;

@SuppressLint("NewApi")
public class AddCategoryDialogFragment extends DialogFragment {

	private Spinner spinner;

	public static AddCategoryDialogFragment newInstance(Spinner spinner) {
		AddCategoryDialogFragment fragment = new AddCategoryDialogFragment();
		fragment.setSpinner(spinner);
		return fragment;
	}

	public void setSpinner(Spinner spinner) {
		this.spinner = spinner;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Add one title to the dialog
		builder.setTitle(R.string.dialog_addcategory_title);
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		final View view = inflater.inflate(R.layout.category_dialog, null);
		builder.setView(view)
				// Add action buttons
				.setPositiveButton(R.string.dialog_addcategory_add, new DialogInterface.OnClickListener() {
					@SuppressWarnings("unchecked")
					public void onClick(DialogInterface dialog, int which) {
						// Add the category informed in the edit text
						// Gets the EditText, then its content and creates an
						// category with its content
						EditText edtCategoryName = (EditText) view.findViewById(R.id.dialog_category);

						for (int i = 0; i < spinner.getCount(); i++) {
							Category c = (Category) spinner.getItemAtPosition(i);
							if (c.getName().equals(edtCategoryName.getText().toString())) {
								spinner.setSelection(i);
								return;
							}
						}

						Category category = new Category();
						try {
							category.setName(edtCategoryName.getText().toString());
							// Controller.instance(getActivity()).addCategory(category);
							if (spinner != null) {
								ArrayAdapter<Category> adapter = (ArrayAdapter<Category>) spinner.getAdapter();
								if (adapter.getCount() >= 2
										&& adapter.getItem(adapter.getCount() - 2).getId() == null) {
									adapter.remove(adapter.getItem(adapter.getCount() - 2));
								}
								adapter.insert(category, adapter.getCount() - 1);
								spinner.setSelection(adapter.getCount() - 2);
							}
						} catch (InvalidTextException e) {
							Log.e("CategoryDialogFragment", e.getMessage()); // TODO:Handle
																				// it
																				// properly.
							e.printStackTrace();
							Toast.makeText(getActivity().getApplicationContext(), "Categoria inválida.",
									Toast.LENGTH_SHORT).show();
							// } catch (DBException e) {
							// Log.e("CategoryDialogFragment", e.getMessage());
							// // TODO:Handle it properly.
							// e.printStackTrace();
							// }
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
						spinner.setSelection(0);
						AddCategoryDialogFragment.this.getDialog().cancel();
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
		// getActivity().recreate();
		// Intent intent = new Intent(getActivity().getApplicationContext(),
		// ReminderMainActivity.class);
		// startActivity(intent);
		// getActivity().finish();
	}
}
