package jp.co.sss.shop.controller.user;


import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class UserRegistCustomerController {
	
	@Autowired
	UserRepository repository;
	
	
	//新規会員登録入力画面に遷移する。
	@RequestMapping(path="/user/regist/input", method=RequestMethod.GET)
	public String userRegistInput( Model model) {
		if (!model.containsAttribute("userForm")) {
			model.addAttribute("userForm", new UserForm());
			return "user/regist/user_regist_input";
		}
		return "user/regist/user_regist_input";
	}
	
	
	//確認画面から「戻る」ボタンを押したときに発動
	@RequestMapping(path="/user/regist/input", method=RequestMethod.POST)
	public String userRegistInputBack(@ModelAttribute UserForm form) {
		return "user/regist/user_regist_input";
	}
	
	
	//入力内容の確認。
	@RequestMapping(path="/user/regist/check", method=RequestMethod.POST)
	public String userDoRegistInput(@Valid @ModelAttribute UserForm form, BindingResult result, HttpSession session, Model model, RedirectAttributes redirectAttributes){
//		//エラーがある場合は前の入力画面に戻る。
		
		if (result.hasErrors()) {
			// 入力チェック結果の情報をフラッシュスコープに保存
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userForm", result);
			// Form クラスの情報をフラッシュスコープに保存
			redirectAttributes.addFlashAttribute("userForm", form);
			return "redirect:/user/regist/input";
		}
		//エラーが無い場合は確認画面に遷移する。
		return "user/regist/user_regist_check";
	}
	
	//入力内容のDBへの登録。
	@RequestMapping(path="/user/regist/complete", method=RequestMethod.POST)
	public String doUserCheck(@Valid @ModelAttribute UserForm form, BindingResult result, HttpSession session) {
		//新規会員情報を格納する「User」エンティティをインスタンス化
		User user= new User();
		
		//登録日設定のため処理
		java.util.Date utilDate = new java.util.Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = Date.valueOf(df.format(utilDate));
		
		//「User」エンティティに格納
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());
		user.setName(form.getName());
		user.setPostalCode(form.getPostalCode());
		user.setAddress(form.getAddress());
		user.setPhoneNumber(form.getPhoneNumber());
		user.setAuthority(2);
		user.setDeleteFlag(0);
		user.setInsertDate(date);
		
		
		repository.save(user);
		
		UserBean userBean = new UserBean();
		
		BeanUtils.copyProperties(user, userBean);
		
		session.setAttribute("user", userBean);
				
		
		return "user/regist/user_regist_complete";
	}
}
