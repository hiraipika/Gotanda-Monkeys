package jp.co.sss.shop.controller.user;


import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class UserUpdateCustomerController {
	
	//会員情報
	@Autowired
	UserRepository userRepository;
	
	//セッション
	@Autowired
	HttpSession session;
	
	
	/**
	 * 会員情報の変更入力画面表示処理
	 *
	 * @param form  会員情報フォーム
	 * @param model Viewとの値受渡し
	 * @return "user/update/user_update_input" 会員情報 変更入力画面へ
	 **/
	@RequestMapping(path="/user/update/input", method=RequestMethod.POST)
	public String userUpdateInput(@ModelAttribute UserForm form, Model model, Integer id) {
		
		User user = userRepository.getById(id);
		session.setAttribute("users", user);
		
		return "user/update/user_update_input";
	}
	
	
	/**
	 * 会員情報 変更確認処理
	 *
	 * @param form   会員情報フォーム
	 * @param model  Viewとの値受渡し
	 * @param result 入力チェック結果
	 * @return 
	 * 入力値エラーあり："user/update/user_update_input" 会員情報変更入力画面へ 
	 * 入力値エラーなし："user/update/user_update_check" 会員情報 変更確認画面へ
	 */
	@RequestMapping(path="/user/update/check", method=RequestMethod.POST)
	public String userDoUpdateInput(@Valid @ModelAttribute UserForm form, BindingResult result, HttpSession session, Model model){

		if(result.hasErrors()) {
			return "user/update/user_update_input";
			
		}

		return "user/update/user_update_check";
	}
	
	//会員情報変更完了画面表示
	@RequestMapping(path="/user/update/complete", method=RequestMethod.POST)
	public String doUserCheck(@Valid @ModelAttribute UserForm form, BindingResult result, HttpSession session) {

		// 変更対象の会員情報を取得
		User user= userRepository.getById(form.getId());
		
		
		//会員情報を登録した日付の取得
		java.util.Date utilDate = new java.util.Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = Date.valueOf(df.format(utilDate));
		
		//入力した情報の更新
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());
		user.setName(form.getName());
		user.setPostalCode(form.getPostalCode());
		user.setAddress(form.getAddress());
		user.setPhoneNumber(form.getPhoneNumber());
		user.setAuthority(2);
		user.setDeleteFlag(0);
		user.setInsertDate(date);
		
		//会員情報を保存
		userRepository.save(user);
		
		return "user/update/user_update_complete";
	}
}
