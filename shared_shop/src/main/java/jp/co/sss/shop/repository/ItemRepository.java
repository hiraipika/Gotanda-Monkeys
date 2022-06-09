package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	/** 商品情報を新着順で検索 */
	public List<Item> findByDeleteFlagOrderByInsertDateDescIdAsc(int deleteFlag);

	/*
	*//** 商品情報を売れ筋順で検索 *//*
							 * @Query("SELECT i FROM Items") public List<Item>
							 * findByItemIdOrderByInsertQuantityDescItemIdAsc();
							 */
}
