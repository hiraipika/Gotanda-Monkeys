package jp.co.sss.shop.controller.item;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.ItemForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
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
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	HttpSession	session;

	
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
	 * 商品一覧画面(新着順) 表示処理
	 * @param model Viewとの値受渡し
	 * @return "category/list/category_list" カテゴリ情報 一覧画面へ
	 */
	@RequestMapping(path = "/item/list/1")
	public String showItemInsertDateDescIdAsc(Model model) {
		// 商品情報を全件検索(新着順)
		List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);

		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/1");
		model.addAttribute("sortType", 1);
		
		return "item/list/item_list";
	}
	/*
	 * 商品一覧画面(売れ筋順) 表示処理
	 */
	@RequestMapping(path = "/item/list/2")
	public String showItemBy(Model model) {
		// 商品情報を全件検索(売れ筋順)
		List<Item> itemList = itemRepository.findByOrder();
		
		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);

		// 商品情報をViewへ渡す
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list/2");
		model.addAttribute("sortType", 2);

		return "item/list/item_list";
	}

	@RequestMapping(path = "/item/list")
	public String showItemAll(Model model) {
		
		return "item/list/item_list";
	}
	
	/*
	 * カテゴリー検索(商品一覧画面以外) 表示処理
	 */
	@RequestMapping(path = "/item/list/category/1", method = RequestMethod.GET)
	public String searchByCategory(Model model, @ModelAttribute ItemForm item) {
		Integer categoryId = Integer.valueOf(item.getCategoryId());
		//
		List<Item> itemList = itemRepository.findByCategoryId(categoryId);
		
		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);
		
		if(!(itemList.size()==0)) {
			// 商品情報をViewへ渡す
			model.addAttribute("items", itemBeanList);
			model.addAttribute("url", "/item/list/1");
			model.addAttribute("sortType", 1);
		}
	    
		return "item/list/item_list";
	}
	
	/*
	 * カテゴリー検索(商品一覧画面) 表示処理
	 */
	@RequestMapping(path = "/item/list/category/{id}")
	public String searchByCategoryOfSort(Model model, @ModelAttribute ItemForm item, @PathVariable int id) {
		Integer categoryId = Integer.valueOf(item.getCategoryId());
		
		if(id == 1) {
			// 商品情報を全件検索(新着順)
			List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDescIdAsc(Constant.NOT_DELETED);

			// エンティティ内の検索結果をJavaBeansにコピー
			List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);
			
			if(!(itemList.size()==0)) {
				// 商品情報をViewへ渡す
				model.addAttribute("items", itemBeanList);
				model.addAttribute("url", "/item/list/1");
				model.addAttribute("sortType", 1);
			}
		}else if(id == 2){
			// 商品情報を全件検索(売れ筋順)
			//List<Item> itemList = itemRepository.findByOrder();
			//itemList = itemRepository.findByCategoryId(categoryId);
			
			List<Item> itemList = itemRepository.findByCategoryId(categoryId);
			//itemList = itemRepository.findByOrder();
			
			// エンティティ内の検索結果をJavaBeansにコピー
			List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList);
			
			if(!(itemList.size()==0)) {
				// 商品情報をViewへ渡す
				model.addAttribute("items", itemBeanList);
				model.addAttribute("url", "/item/list/2");
				model.addAttribute("sortType", 2);
			}
		}
		
		return "item/list/item_list";
	}
}
