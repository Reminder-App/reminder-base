package br.unb.cic.reminders.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.unb.cic.reminders.model.Category;
import br.unb.cic.reminders2.R;

public class CategoryArrayAdapter extends ArrayAdapter<Category> {

	private Context context;

	/**
	 * This class is used in the parts of the view which is needed to list the
	 * categories, as an adapter to these components listing the Categories
	 * items.
	 * 
	 * @param context,
	 *            and the reminders objects(List<Category>).
	 * 
	 * @author positivo
	 * 
	 */
	public CategoryArrayAdapter(Context context, List<Category> objects) {
		super(context, R.layout.category_row, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout categoryRow;

		// Inflate the view
		if (convertView == null) {
			categoryRow = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(R.layout.category_row, categoryRow, true);
		} else {
			categoryRow = (LinearLayout) convertView;
		}

		TextView tvCategory = (TextView) categoryRow.findViewById(R.id.row_categoryName);

		tvCategory.setText(getItem(position).getName());

		return categoryRow;
	}

}
