package jp.co.sss.shop.controller.item;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.util.BeanCopy;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ItemShowCustomerController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	
	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "/" トップ画面へ
	 */
	@RequestMapping(path = "/")
	public String index(Model model) {

		
		return "index";
	}
	
	@RequestMapping(path = "/item/detail")
	public String showItem(Model model, Pageable pageable) {
	// 商品情報を全件検索(新着順)
			List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

			// エンティティ内の検索結果をJavaBeansにコピー
			List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);

			// 商品情報をViewへ渡す
			model.addAttribute("items", itemBeanList);
			model.addAttribute("url", "/item/detatil/");
			
			return "item/list/item_detail";
			}
	
	/**
	 * 商品情報詳細表示処理
	 *
	 * @param id 商品ID
	 * @param model Viewとの値受渡し
	 * @param session セッション情報
	 * @return "/item/detail/item_detail_admin" 商品情報 詳細画面へ
	 */
	
	@RequestMapping(path = "/item/detail/{id}")
	public String showItem(@PathVariable int id, Model model) {

<<<<<<< HEAD
		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.getById(id);

		ItemBean itemBean = new ItemBean();

		// Itemエンティティの各フィールドの値をItemBeanにコピー
		BeanUtils.copyProperties(item, itemBean);

		// 商品情報にカテゴリ名を設定
		itemBean.setCategoryName(item.getCategory().getName());

		// 商品情報をViewへ渡す
		model.addAttribute("item", itemBean);

		return "item/detail/item_detail";
	}
	
	/*
	 * 商品一覧画面 表示処理
	 * @param model Viewとの値受渡し
	 * @return "category/list/category_list" カテゴリ情報 一覧画面へ
	 */
}
=======
	@RequestMapping(path = "/item/list")
	public String showItemAll(Model model) {
		
		return "item/list/item_list";
	}
}
>>>>>>> 9af329b94d1b0c440bb6e22ab2af49c277a42054
