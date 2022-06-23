package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {


	public List<Item> findByDeleteFlagOrderByInsertDateDescIdAsc(int deleteFlag);

	/** 商品情報を売れ筋順で検索 */
	@Query(value = "SELECT * FROM ITEMS LEFT JOIN (SELECT ITEMS.ID ITEM_ID, COUNT(ITEM_ID) COUNT FROM ITEMS LEFT JOIN ORDER_ITEMS ON ITEMS.ID = ORDER_ITEMS.ITEM_ID GROUP BY ITEM_ID, ITEMS.ID) T1 ON ITEMS.ID = T1.ITEM_ID WHERE DELETE_FLAG = 0 ORDER BY COUNT DESC,ID ASC", nativeQuery = true)
	public List<Item> findByOrder();
	
	/** カテゴリー検索 */
	@Query(value = "SELECT * FROM ITEMS WHERE DELETE_FLAG = 0 AND ITEMS.CATEGORY_ID = :categoryId", nativeQuery = true)
	public List<Item> findByCategoryId(@Param("categoryId") Integer categoryId);
	
	/** 売れ筋順のカテゴリーで検索 */
	@Query(value = "SELECT * FROM ITEMS LEFT JOIN (SELECT ITEMS.ID ITEM_ID, COUNT(ITEM_ID) COUNT FROM ITEMS LEFT JOIN ORDER_ITEMS ON ITEMS.ID = ORDER_ITEMS.ITEM_ID GROUP BY ITEM_ID, ITEMS.ID) T1 ON ITEMS.ID = T1.ITEM_ID WHERE DELETE_FLAG = 0 AND ITEMS.CATEGORY_ID = :categoryId ORDER BY COUNT DESC,ID ASC", nativeQuery = true)
	public List<Item> findByOrderOfCategory(@Param("categoryId") Integer categoryId);
	
	/** 在庫ストックをオーダー分減らす */
	@Transactional
	@Modifying
	@Query("UPDATE Item SET stock = (:stock - :orderItemList) WHERE id = :id")
	public default Integer decreaseByOrder(@Param("orderItemList") Integer orderItemList, @Param("stock") Integer stock, @Param("id") Integer id) {
		Integer newStock = stock - orderItemList;
		return newStock;
	}}
