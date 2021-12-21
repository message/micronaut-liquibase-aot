package com.example.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BaseRepository
{

	private static volatile int aliasCount = 0;

	public static <T> CriteriaQuery<Long> countCriteria(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteria)
	{
		CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
		copyCriteriaWithoutSelectionAndOrder(criteria, countCriteria, false);

		Expression<Long> countExpression;

		if (criteria.isDistinct()) {
			countExpression = criteriaBuilder.countDistinct(findRoot(countCriteria, criteria.getResultType()));
		} else {
			countExpression = criteriaBuilder.count(findRoot(countCriteria, criteria.getResultType()));
		}

		return countCriteria.select(countExpression);
	}

	/**
	 * Copy criteria without selection and order.
	 *
	 * @param from source Criteria.
	 * @param to   destination Criteria.
	 */
	public static void copyCriteriaWithoutSelectionAndOrder(CriteriaQuery<?> from, CriteriaQuery<?> to, boolean copyFetches)
	{
		// Copy Roots
		for (Root<?> root : from.getRoots()) {
			Root<?> dest = to.from(root.getJavaType());
			dest.alias(getOrCreateAlias(root));
			copyJoins(root, dest);
			if (copyFetches) {
				copyFetches(root, dest);
			}
		}

		to.groupBy(from.getGroupList());
		to.distinct(from.isDistinct());

		if (from.getGroupRestriction() != null) {
			to.having(from.getGroupRestriction());
		}

		Predicate predicate = from.getRestriction();
		if (predicate != null) {
			to.where(predicate);

		}
	}

	/**
	 * Gets The result alias, if none set a default one and return it
	 *
	 * @param selection
	 *
	 * @return root alias or generated one
	 */
	private static synchronized <T> String getOrCreateAlias(Selection<T> selection)
	{
		// reset alias count
		if (aliasCount > 1000)
			aliasCount = 0;

		String alias = selection.getAlias();
		if (alias == null) {
			alias = "JDAL_generatedAlias" + aliasCount++;
			selection.alias(alias);
		}

		return alias;

	}

	/**
	 * Find the Root with type class on CriteriaQuery Root Set
	 *
	 * @param <T>   root type
	 * @param query criteria query
	 * @param clazz root type
	 *
	 * @return Root<T> of null if none
	 */
	@SuppressWarnings("unchecked")
	private static <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz)
	{
		for (Root<?> r : query.getRoots()) {
			if (clazz.equals(r.getJavaType())) {
				return (Root<T>) r.as(clazz);
			}
		}
		return null;
	}

	/**
	 * Copy Criteria without Selection.
	 *
	 * @param from source Criteria.
	 * @param to   destination Criteria.
	 */
	private static void copyCriteriaNoSelection(CriteriaQuery<?> from, CriteriaQuery<?> to)
	{
		copyCriteriaWithoutSelectionAndOrder(from, to, true);
		to.orderBy(from.getOrderList());
	}

	private static <T> void copyCriteria(CriteriaQuery<T> from, CriteriaQuery<T> to)
	{
		copyCriteriaNoSelection(from, to);
		to.select(from.getSelection());
	}

	/**
	 * Copy Joins
	 *
	 * @param from source Join
	 * @param to   destination Join
	 */
	private static void copyJoins(From<?, ?> from, From<?, ?> to)
	{
		for (Join<?, ?> join : from.getJoins()) {
			Join<?, ?> toJoin = to.join(join.getAttribute().getName(), join.getJoinType());
			toJoin.alias(getOrCreateAlias(join));

			copyJoins(join, toJoin);
		}
	}

	/**
	 * Copy Fetches
	 *
	 * @param from source From
	 * @param to   destination From
	 */
	private static void copyFetches(From<?, ?> from, From<?, ?> to)
	{
		for (Fetch<?, ?> f : from.getFetches()) {
			Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
			copyFetches(f, toFetch);
		}
	}

	/**
	 * Copy Fetches
	 *
	 * @param from source Fetch
	 * @param to   dest Fetch
	 */
	private static void copyFetches(Fetch<?, ?> from, Fetch<?, ?> to)
	{
		for (Fetch<?, ?> f : from.getFetches()) {
			Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
			// recursively copy fetches
			copyFetches(f, toFetch);
		}
	}

	public static Integer calculatePageCount(long items, int perPage)
	{
		BigDecimal divide = new BigDecimal(perPage + items - 1);

		return divide.divide(new BigDecimal(perPage), 0, RoundingMode.DOWN).intValue();
	}

}
