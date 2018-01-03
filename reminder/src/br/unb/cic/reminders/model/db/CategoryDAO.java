package br.unb.cic.reminders.model.db;

import java.util.List;

import br.unb.cic.framework.persistence.DBException;
import br.unb.cic.reminders.model.Category;

/**
 * A Data Access Object for handling the persistence mechanism of categories.
 * 
 * @author rbonifacio
 */
public interface CategoryDAO {

	/**
	 * Save a category in the reminders database.
	 * 
	 * @param category
	 *            - the category that will be stored.
	 */
	public void saveCategory(Category category) throws DBException;

	/**
	 * List all Categories of the Reminders database.
	 * 
	 * @return list of registered categories.
	 * @throws DBException
	 *             if anything goes wrong with the database query
	 */
	public List<Category> listCategories() throws DBException;

	/**
	 * Find a category by name
	 * 
	 * @param name
	 *            category name
	 * @return category that satisfies the name criteria
	 */
	public Category findCategory(String name) throws DBException;

	/**
	 * Find a category by id
	 * 
	 * @param id
	 *            category id
	 * @return category that satisfies the id criteria
	 */
	public Category findCategoryById(Long id) throws DBException;

	/**
	 * Update a given gategory from the database
	 * 
	 * @param category
	 *            the category that will be updated from the database
	 * @throws DBException
	 *             if anything goes wrong with the database query
	 */
	public void updateCategory(Category category) throws DBException;

	/**
	 * Delete a given gategory from the database
	 * 
	 * @param category
	 *            the category that will be dropped from the database
	 * @throws DBException
	 *             if anything goes wrong with the database query
	 */
	public void deleteCategory(Category category) throws DBException;
}
