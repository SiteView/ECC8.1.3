package com.siteview.ecc.tasks;

import java.util.ArrayList;
import java.util.Date;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;

public class EditRelTask extends GenericForwardComposer {

	private Listbox listbox_data;
	private Checkbox flag1,m0,m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12,m13,m14,m15,m16,m17,m18,m19,m20,m21,m22,m23;
	private Checkbox flag2,t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23;
	private Checkbox flag3,w0,w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11,w12,w13,w14,w15,w16,w17,w18,w19,w20,w21,w22,w23;
	private Checkbox flag4,s0,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19,s20,s21,s22,s23;
	private Checkbox flag5,f0,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f19,f20,f21,f22,f23;
	private Checkbox flag6,sa0,sa1,sa2,sa3,sa4,sa5,sa6,sa7,sa8,sa9,sa10,sa11,sa12,sa13,sa14,sa15,sa16,sa17,sa18,sa19,sa20,sa21,sa22,sa23;
	private Checkbox flag7,su0,su1,su2,su3,su4,su5,su6,su7,su8,su9,su10,su11,su12,su13,su14,su15,su16,su17,su18,su19,su20,su21,su22,su23;
	
	private Textbox nameTextbox;
	private Textbox description;
	private Window editRelativeTask;
	private Include eccBody;
	
	public Listbox getListbox_data() {
		return listbox_data;
	}

	public void setListbox_data(Listbox listbox_data) {
		this.listbox_data = listbox_data;
	}
	
	public void onShowCheckTime1(Event event){
		if(flag1.isChecked()){
			m0.setChecked(true);
			m1.setChecked(true);
			m2.setChecked(true);
			m3.setChecked(true);
			m4.setChecked(true);
			m5.setChecked(true);
			m6.setChecked(true);
			m7.setChecked(true);
			m8.setChecked(true);
			m9.setChecked(true);
			m10.setChecked(true);
			m11.setChecked(true);
			m12.setChecked(true);
			m13.setChecked(true);
			m14.setChecked(true);
			m15.setChecked(true);
			m16.setChecked(true);
			m17.setChecked(true);
			m18.setChecked(true);
			m19.setChecked(true);
			m20.setChecked(true);
			m21.setChecked(true);
			m22.setChecked(true);
			m23.setChecked(true);
			
		}else{
			m0.setChecked(false);
			m1.setChecked(false);
			m2.setChecked(false);
			m3.setChecked(false);
			m4.setChecked(false);
			m5.setChecked(false);
			m6.setChecked(false);
			m7.setChecked(false);
			m8.setChecked(false);
			m9.setChecked(false);	
			m10.setChecked(false);
			m11.setChecked(false);
			m12.setChecked(false);
			m13.setChecked(false);
			m14.setChecked(false);
			m15.setChecked(false);
			m16.setChecked(false);
			m17.setChecked(false);
			m18.setChecked(false);
			m19.setChecked(false);
			m20.setChecked(false);
			m21.setChecked(false);
			m22.setChecked(false);
			m23.setChecked(false);			
		}
	}
	public void onShowCheckTime(Event event){
		if(!m0.isChecked()||!m1.isChecked()||!m2.isChecked()||!m3.isChecked()||!m4.isChecked()||!m5.isChecked()||!m6.isChecked()||
				!m7.isChecked()||!m8.isChecked()||!m9.isChecked()||!m10.isChecked()||!m11.isChecked()||!m12.isChecked()||!m13.isChecked()||
				!m14.isChecked()||!m15.isChecked()||!m15.isChecked()||!m17.isChecked()||!m18.isChecked()||!m19.isChecked()||!m20.isChecked()||
				!m21.isChecked()||!m22.isChecked()||!m23.isChecked()){
			flag1.setChecked(false);
		}
		if(m0.isChecked()&&m1.isChecked()&&m2.isChecked()&&m3.isChecked()&&m4.isChecked()&&m5.isChecked()&&m6.isChecked()&&
				m7.isChecked()&&m8.isChecked()&&m9.isChecked()&&m10.isChecked()&&m11.isChecked()&&m12.isChecked()&&m13.isChecked()&&
				m14.isChecked()&&m15.isChecked()&&m15.isChecked()&&m17.isChecked()&&m18.isChecked()&&m19.isChecked()&&m20.isChecked()&&
				m21.isChecked()&&m22.isChecked()&&m23.isChecked()){
			flag1.setChecked(true);
		}
		
		if(!t0.isChecked()||!t1.isChecked()||!t2.isChecked()||!t3.isChecked()||!t4.isChecked()||!t5.isChecked()||!t6.isChecked()||
				!t7.isChecked()||!t8.isChecked()||!t9.isChecked()||!t10.isChecked()||!t11.isChecked()||!t12.isChecked()||!t13.isChecked()||
				!t14.isChecked()||!t15.isChecked()||!t15.isChecked()||!t17.isChecked()||!t18.isChecked()||!t19.isChecked()||!t20.isChecked()||
				!t21.isChecked()||!t22.isChecked()||!t23.isChecked()){
			flag2.setChecked(false);
		}
		if(t0.isChecked()&&t1.isChecked()&&t2.isChecked()&&t3.isChecked()&&t4.isChecked()&&t5.isChecked()&&t6.isChecked()&&
				t7.isChecked()&&t8.isChecked()&&t9.isChecked()&&t10.isChecked()&&t11.isChecked()&&t12.isChecked()&&t13.isChecked()&&
				t14.isChecked()&&t15.isChecked()&&t15.isChecked()&&t17.isChecked()&&t18.isChecked()&&t19.isChecked()&&t20.isChecked()&&
				t21.isChecked()&&t22.isChecked()&&t23.isChecked()){
			flag2.setChecked(true);
		}	
		if(!w0.isChecked()||!w1.isChecked()||!w2.isChecked()||!w3.isChecked()||!w4.isChecked()||!w5.isChecked()||!w6.isChecked()||
				!w7.isChecked()||!w8.isChecked()||!w9.isChecked()||!w10.isChecked()||!w11.isChecked()||!w12.isChecked()||!w13.isChecked()||
				!w14.isChecked()||!w15.isChecked()||!w15.isChecked()||!w17.isChecked()||!w18.isChecked()||!w19.isChecked()||!w20.isChecked()||
				!w21.isChecked()||!w22.isChecked()||!w23.isChecked()){
			flag3.setChecked(false);
		}
		if(w0.isChecked()&&w1.isChecked()&&w2.isChecked()&&w3.isChecked()&&w4.isChecked()&&w5.isChecked()&&w6.isChecked()&&
				w7.isChecked()&&w8.isChecked()&&w9.isChecked()&&w10.isChecked()&&w11.isChecked()&&w12.isChecked()&&w13.isChecked()&&
				w14.isChecked()&&w15.isChecked()&&w15.isChecked()&&w17.isChecked()&&w18.isChecked()&&w19.isChecked()&&w20.isChecked()&&
				w21.isChecked()&&w22.isChecked()&&w23.isChecked()){
			flag3.setChecked(true);
		}	
		if(!s0.isChecked()||!s1.isChecked()||!s2.isChecked()||!s3.isChecked()||!s4.isChecked()||!s5.isChecked()||!s6.isChecked()||
				!s7.isChecked()||!s8.isChecked()||!s9.isChecked()||!s10.isChecked()||!s11.isChecked()||!s12.isChecked()||!s13.isChecked()||
				!s14.isChecked()||!s15.isChecked()||!s15.isChecked()||!s17.isChecked()||!s18.isChecked()||!s19.isChecked()||!s20.isChecked()||
				!s21.isChecked()||!s22.isChecked()||!s23.isChecked()){
			flag4.setChecked(false);
		}
		if(s0.isChecked()&&s1.isChecked()&&s2.isChecked()&&s3.isChecked()&&s4.isChecked()&&s5.isChecked()&&s6.isChecked()&&
				s7.isChecked()&&s8.isChecked()&&s9.isChecked()&&s10.isChecked()&&s11.isChecked()&&s12.isChecked()&&s13.isChecked()&&
				s14.isChecked()&&s15.isChecked()&&s15.isChecked()&&s17.isChecked()&&s18.isChecked()&&s19.isChecked()&&s20.isChecked()&&
				s21.isChecked()&&s22.isChecked()&&s23.isChecked()){
			flag4.setChecked(true);
		}	
		if(!f0.isChecked()||!f1.isChecked()||!f2.isChecked()||!f3.isChecked()||!f4.isChecked()||!f5.isChecked()||!f6.isChecked()||
				!f7.isChecked()||!f8.isChecked()||!f9.isChecked()||!f10.isChecked()||!f11.isChecked()||!f12.isChecked()||!f13.isChecked()||
				!f14.isChecked()||!f15.isChecked()||!f15.isChecked()||!f17.isChecked()||!f18.isChecked()||!f19.isChecked()||!f20.isChecked()||
				!f21.isChecked()||!f22.isChecked()||!f23.isChecked()){
			flag5.setChecked(false);
		}
		if(f0.isChecked()&&f1.isChecked()&&f2.isChecked()&&f3.isChecked()&&f4.isChecked()&&f5.isChecked()&&f6.isChecked()&&
				f7.isChecked()&&f8.isChecked()&&f9.isChecked()&&f10.isChecked()&&f11.isChecked()&&f12.isChecked()&&f13.isChecked()&&
				f14.isChecked()&&f15.isChecked()&&f15.isChecked()&&f17.isChecked()&&f18.isChecked()&&f19.isChecked()&&f20.isChecked()&&
				f21.isChecked()&&f22.isChecked()&&f23.isChecked()){
			flag5.setChecked(true);
		}	
		if(!sa0.isChecked()||!sa1.isChecked()||!sa2.isChecked()||!sa3.isChecked()||!sa4.isChecked()||!sa5.isChecked()||!sa6.isChecked()||
				!sa7.isChecked()||!sa8.isChecked()||!sa9.isChecked()||!sa10.isChecked()||!sa11.isChecked()||!sa12.isChecked()||!sa13.isChecked()||
				!sa14.isChecked()||!sa15.isChecked()||!sa15.isChecked()||!sa17.isChecked()||!sa18.isChecked()||!sa19.isChecked()||!sa20.isChecked()||
				!sa21.isChecked()||!sa22.isChecked()||!sa23.isChecked()){
			flag6.setChecked(false);
		}
		if(sa0.isChecked()&&sa1.isChecked()&&sa2.isChecked()&&sa3.isChecked()&&sa4.isChecked()&&sa5.isChecked()&&sa6.isChecked()&&
				sa7.isChecked()&&sa8.isChecked()&&sa9.isChecked()&&sa10.isChecked()&&sa11.isChecked()&&sa12.isChecked()&&sa13.isChecked()&&
				sa14.isChecked()&&sa15.isChecked()&&sa15.isChecked()&&sa17.isChecked()&&sa18.isChecked()&&sa19.isChecked()&&sa20.isChecked()&&
				sa21.isChecked()&&sa22.isChecked()&&sa23.isChecked()){
			flag6.setChecked(true);
		}
		if(!su0.isChecked()||!su1.isChecked()||!su2.isChecked()||!su3.isChecked()||!su4.isChecked()||!su5.isChecked()||!su6.isChecked()||
				!su7.isChecked()||!su8.isChecked()||!su9.isChecked()||!su10.isChecked()||!su11.isChecked()||!su12.isChecked()||!su13.isChecked()||
				!su14.isChecked()||!su15.isChecked()||!su15.isChecked()||!su17.isChecked()||!su18.isChecked()||!su19.isChecked()||!su20.isChecked()||
				!su21.isChecked()||!su22.isChecked()||!su23.isChecked()){
			flag7.setChecked(false);
		}
		if(su0.isChecked()&&su1.isChecked()&&su2.isChecked()&&su3.isChecked()&&su4.isChecked()&&su5.isChecked()&&su6.isChecked()&&
				su7.isChecked()&&su8.isChecked()&&su9.isChecked()&&su10.isChecked()&&su11.isChecked()&&su12.isChecked()&&su13.isChecked()&&
				su14.isChecked()&&su15.isChecked()&&su15.isChecked()&&su17.isChecked()&&su18.isChecked()&&su19.isChecked()&&su20.isChecked()&&
				su21.isChecked()&&su22.isChecked()&&su23.isChecked()){
			flag7.setChecked(true);
		}
	}
	
	public void onShowCheckTite2(Event event){
		if(flag2.isChecked()){
			t0.setChecked(true);
			t1.setChecked(true);
			t2.setChecked(true);
			t3.setChecked(true);
			t4.setChecked(true);
			t5.setChecked(true);
			t6.setChecked(true);
			t7.setChecked(true);
			t8.setChecked(true);
			t9.setChecked(true);
			t10.setChecked(true);
			t11.setChecked(true);
			t12.setChecked(true);
			t13.setChecked(true);
			t14.setChecked(true);
			t15.setChecked(true);
			t16.setChecked(true);
			t17.setChecked(true);
			t18.setChecked(true);
			t19.setChecked(true);
			t20.setChecked(true);
			t21.setChecked(true);
			t22.setChecked(true);
			t23.setChecked(true);
			
		}else{
			t0.setChecked(false);
			t1.setChecked(false);
			t2.setChecked(false);
			t3.setChecked(false);
			t4.setChecked(false);
			t5.setChecked(false);
			t6.setChecked(false);
			t7.setChecked(false);
			t8.setChecked(false);
			t9.setChecked(false);	
			t10.setChecked(false);
			t11.setChecked(false);
			t12.setChecked(false);
			t13.setChecked(false);
			t14.setChecked(false);
			t15.setChecked(false);
			t16.setChecked(false);
			t17.setChecked(false);
			t18.setChecked(false);
			t19.setChecked(false);
			t20.setChecked(false);
			t21.setChecked(false);
			t22.setChecked(false);
			t23.setChecked(false);			
		}
	}	
	public void onShowCheckTime3(Event event){
		if(flag3.isChecked()){
			w0.setChecked(true);
			w1.setChecked(true);
			w2.setChecked(true);
			w3.setChecked(true);
			w4.setChecked(true);
			w5.setChecked(true);
			w6.setChecked(true);
			w7.setChecked(true);
			w8.setChecked(true);
			w9.setChecked(true);
			w10.setChecked(true);
			w11.setChecked(true);
			w12.setChecked(true);
			w13.setChecked(true);
			w14.setChecked(true);
			w15.setChecked(true);
			w16.setChecked(true);
			w17.setChecked(true);
			w18.setChecked(true);
			w19.setChecked(true);
			w20.setChecked(true);
			w21.setChecked(true);
			w22.setChecked(true);
			w23.setChecked(true);
			
		}else{
			w0.setChecked(false);
			w1.setChecked(false);
			w2.setChecked(false);
			w3.setChecked(false);
			w4.setChecked(false);
			w5.setChecked(false);
			w6.setChecked(false);
			w7.setChecked(false);
			w8.setChecked(false);
			w9.setChecked(false);	
			w10.setChecked(false);
			w11.setChecked(false);
			w12.setChecked(false);
			w13.setChecked(false);
			w14.setChecked(false);
			w15.setChecked(false);
			w16.setChecked(false);
			w17.setChecked(false);
			w18.setChecked(false);
			w19.setChecked(false);
			w20.setChecked(false);
			w21.setChecked(false);
			w22.setChecked(false);
			w23.setChecked(false);			
		}
	}
	public void onShowCheckTime4(Event event){
		if(flag4.isChecked()){
			s0.setChecked(true);
			s1.setChecked(true);
			s2.setChecked(true);
			s3.setChecked(true);
			s4.setChecked(true);
			s5.setChecked(true);
			s6.setChecked(true);
			s7.setChecked(true);
			s8.setChecked(true);
			s9.setChecked(true);
			s10.setChecked(true);
			s11.setChecked(true);
			s12.setChecked(true);
			s13.setChecked(true);
			s14.setChecked(true);
			s15.setChecked(true);
			s16.setChecked(true);
			s17.setChecked(true);
			s18.setChecked(true);
			s19.setChecked(true);
			s20.setChecked(true);
			s21.setChecked(true);
			s22.setChecked(true);
			s23.setChecked(true);
			
		}else{
			s0.setChecked(false);
			s1.setChecked(false);
			s2.setChecked(false);
			s3.setChecked(false);
			s4.setChecked(false);
			s5.setChecked(false);
			s6.setChecked(false);
			s7.setChecked(false);
			s8.setChecked(false);
			s9.setChecked(false);	
			s10.setChecked(false);
			s11.setChecked(false);
			s12.setChecked(false);
			s13.setChecked(false);
			s14.setChecked(false);
			s15.setChecked(false);
			s16.setChecked(false);
			s17.setChecked(false);
			s18.setChecked(false);
			s19.setChecked(false);
			s20.setChecked(false);
			s21.setChecked(false);
			s22.setChecked(false);
			s23.setChecked(false);			
		}
	}	
	public void onShowCheckTime5(Event event){
		if(flag5.isChecked()){
			f0.setChecked(true);
			f1.setChecked(true);
			f2.setChecked(true);
			f3.setChecked(true);
			f4.setChecked(true);
			f5.setChecked(true);
			f6.setChecked(true);
			f7.setChecked(true);
			f8.setChecked(true);
			f9.setChecked(true);
			f10.setChecked(true);
			f11.setChecked(true);
			f12.setChecked(true);
			f13.setChecked(true);
			f14.setChecked(true);
			f15.setChecked(true);
			f16.setChecked(true);
			f17.setChecked(true);
			f18.setChecked(true);
			f19.setChecked(true);
			f20.setChecked(true);
			f21.setChecked(true);
			f22.setChecked(true);
			f23.setChecked(true);
			
		}else{
			f0.setChecked(false);
			f1.setChecked(false);
			f2.setChecked(false);
			f3.setChecked(false);
			f4.setChecked(false);
			f5.setChecked(false);
			f6.setChecked(false);
			f7.setChecked(false);
			f8.setChecked(false);
			f9.setChecked(false);	
			f10.setChecked(false);
			f11.setChecked(false);
			f12.setChecked(false);
			f13.setChecked(false);
			f14.setChecked(false);
			f15.setChecked(false);
			f16.setChecked(false);
			f17.setChecked(false);
			f18.setChecked(false);
			f19.setChecked(false);
			f20.setChecked(false);
			f21.setChecked(false);
			f22.setChecked(false);
			f23.setChecked(false);			
		}
	}	
	public void onShowCheckTime6(Event event){
		if(flag6.isChecked()){
			sa0.setChecked(true);
			sa1.setChecked(true);
			sa2.setChecked(true);
			sa3.setChecked(true);
			sa4.setChecked(true);
			sa5.setChecked(true);
			sa6.setChecked(true);
			sa7.setChecked(true);
			sa8.setChecked(true);
			sa9.setChecked(true);
			sa10.setChecked(true);
			sa11.setChecked(true);
			sa12.setChecked(true);
			sa13.setChecked(true);
			sa14.setChecked(true);
			sa15.setChecked(true);
			sa16.setChecked(true);
			sa17.setChecked(true);
			sa18.setChecked(true);
			sa19.setChecked(true);
			sa20.setChecked(true);
			sa21.setChecked(true);
			sa22.setChecked(true);
			sa23.setChecked(true);
			
		}else{
			sa0.setChecked(false);
			sa1.setChecked(false);
			sa2.setChecked(false);
			sa3.setChecked(false);
			sa4.setChecked(false);
			sa5.setChecked(false);
			sa6.setChecked(false);
			sa7.setChecked(false);
			sa8.setChecked(false);
			sa9.setChecked(false);	
			sa10.setChecked(false);
			sa11.setChecked(false);
			sa12.setChecked(false);
			sa13.setChecked(false);
			sa14.setChecked(false);
			sa15.setChecked(false);
			sa16.setChecked(false);
			sa17.setChecked(false);
			sa18.setChecked(false);
			sa19.setChecked(false);
			sa20.setChecked(false);
			sa21.setChecked(false);
			sa22.setChecked(false);
			sa23.setChecked(false);			
		}
	}	
	public void onShowCheckTime7(Event event){
		if(flag7.isChecked()){
			su0.setChecked(true);
			su1.setChecked(true);
			su2.setChecked(true);
			su3.setChecked(true);
			su4.setChecked(true);
			su5.setChecked(true);
			su6.setChecked(true);
			su7.setChecked(true);
			su8.setChecked(true);
			su9.setChecked(true);
			su10.setChecked(true);
			su11.setChecked(true);
			su12.setChecked(true);
			su13.setChecked(true);
			su14.setChecked(true);
			su15.setChecked(true);
			su16.setChecked(true);
			su17.setChecked(true);
			su18.setChecked(true);
			su19.setChecked(true);
			su20.setChecked(true);
			su21.setChecked(true);
			su22.setChecked(true);
			su23.setChecked(true);
			
		}else{
			su0.setChecked(false);
			su1.setChecked(false);
			su2.setChecked(false);
			su3.setChecked(false);
			su4.setChecked(false);
			su5.setChecked(false);
			su6.setChecked(false);
			su7.setChecked(false);
			su8.setChecked(false);
			su9.setChecked(false);	
			su10.setChecked(false);
			su11.setChecked(false);
			su12.setChecked(false);
			su13.setChecked(false);
			su14.setChecked(false);
			su15.setChecked(false);
			su16.setChecked(false);
			su17.setChecked(false);
			su18.setChecked(false);
			su19.setChecked(false);
			su20.setChecked(false);
			su21.setChecked(false);
			su22.setChecked(false);
			su23.setChecked(false);			
		}
	}	
	
	public void onInit() throws Exception {
		try{
			if (editRelativeTask.getAttribute("flag").equals("edit")) {
				TaskPack tp = new TaskPack();
				Task t = tp.findByName((String) editRelativeTask.getAttribute("itemId"));
				nameTextbox.setValue(t.getName());
				nameTextbox.setDisabled(true);

				description.setValue(t.getDescription());

				String monday=t.getStart0();
				displayChecked(monday,m0,m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12,m13,m14,m15,m16,m17,m18,m19,m20,m21,m22,m23);
				String tuesday=t.getStart1();
				displayChecked(tuesday,t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23);
				String sursday=t.getStart2();
				displayChecked(sursday,w0,w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11,w12,w13,w14,w15,w16,w17,w18,w19,w20,w21,w22,w23);
				String wensday=t.getStart3();
				displayChecked(wensday,s0,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19,s20,s21,s22,s23);
				String friday=t.getStart4();
				displayChecked(friday,f0,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f19,f20,f21,f22,f23);
				String saturday=t.getStart5();
				displayChecked(saturday,sa0,sa1,sa2,sa3,sa4,sa5,sa6,sa7,sa8,sa9,sa10,sa11,sa12,sa13,sa14,sa15,sa16,sa17,sa18,sa19,sa20,sa21,sa22,sa23);
				String sunday=t.getStart6();
				displayChecked(sunday,su0,su1,su2,su3,su4,su5,su6,su7,su8,su9,su10,su11,su12,su13,su14,su15,su16,su17,su18,su19,su20,su21,su22,su23);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}	
	
	public void displayChecked(String value,Checkbox c0,Checkbox c1,Checkbox c2,Checkbox c3,Checkbox c4,Checkbox c5,Checkbox c6,Checkbox c7,
			Checkbox c8,Checkbox c9,Checkbox c10,Checkbox c11,Checkbox c12,Checkbox c13,Checkbox c14,Checkbox c15,Checkbox c16,Checkbox c17,
			Checkbox c18,Checkbox c19,Checkbox c20,Checkbox c21,Checkbox c22,Checkbox c23){
		
		String timeValue=value;
		if("1".equals(timeValue.substring(0, 1))){
			c0.setChecked(true);
		}
		if("1".equals(timeValue.substring(1, 2))){
			c1.setChecked(true);
		}
		if("1".equals(timeValue.substring(2, 3))){
			c2.setChecked(true);
		}
		if("1".equals(timeValue.substring(3, 4))){
			c3.setChecked(true);
		}
		if("1".equals(timeValue.substring(4, 5))){
			c4.setChecked(true);
		}
		if("1".equals(timeValue.substring(5, 6))){
			c5.setChecked(true);
		}
		if("1".equals(timeValue.substring(6, 7))){
			c6.setChecked(true);
		}
		if("1".equals(timeValue.substring(7, 8))){
			c7.setChecked(true);
		}
		if("1".equals(timeValue.substring(8, 9))){
			c8.setChecked(true);
		}
		if("1".equals(timeValue.substring(9, 10))){
			c9.setChecked(true);
		}
		if("1".equals(timeValue.substring(10, 11))){
			c10.setChecked(true);
		}
		if("1".equals(timeValue.substring(11, 12))){
			c11.setChecked(true);
		}
		if("1".equals(timeValue.substring(12, 13))){
			c12.setChecked(true);
		}
		if("1".equals(timeValue.substring(13, 14))){
			c13.setChecked(true);
		}
		if("1".equals(timeValue.substring(14, 15))){
			c14.setChecked(true);
		}
		if("1".equals(timeValue.substring(15, 16))){
			c15.setChecked(true);
		}
		if("1".equals(timeValue.substring(16, 17))){
			c16.setChecked(true);
		}
		if("1".equals(timeValue.substring(17, 18))){
			c17.setChecked(true);
		}
		if("1".equals(timeValue.substring(18, 19))){
			c18.setChecked(true);
		}
		if("1".equals(timeValue.substring(19, 20))){
			c19.setChecked(true);
		}
		if("1".equals(timeValue.substring(20, 21))){
			c20.setChecked(true);
		}
		if("1".equals(timeValue.substring(21, 22))){
			c21.setChecked(true);
		}
		if("1".equals(timeValue.substring(22, 23))){
			c22.setChecked(true);
		}
		if("1".equals(timeValue.substring(23, 24))){
			c23.setChecked(true);
		}

		
		
	}
	
	public void onApply(Event event) throws Exception {
		ArrayList<String> abTaskList = (ArrayList<String>)editRelativeTask.getAttribute("abTaskList");
		ArrayList<String> perTaskList = (ArrayList<String>)editRelativeTask.getAttribute("perTaskList");
		ArrayList<String> reTaskList = (ArrayList<String>)editRelativeTask.getAttribute("reTaskList");
		if(perTaskList == null){
			abTaskList = new ArrayList<String>();
			perTaskList = new ArrayList<String>();
			reTaskList = new ArrayList<String>();
		}
		String nameTextboxValue = nameTextbox.getValue().trim();
		
		if ("".equals(nameTextboxValue)) {
			Messagebox.show("任务计划名称不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			nameTextbox.focus();
			return;
		} else if (((String) editRelativeTask.getAttribute("flag")).equals("add") && reTaskList.contains(nameTextboxValue)) {
			Messagebox.show("该名称已经存在,请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			nameTextbox.focus();
			return;
		} else if (((String)editRelativeTask.getAttribute("flag")).equals("add") && (abTaskList.contains(nameTextboxValue) || perTaskList.contains(nameTextboxValue))){
			try{
				Messagebox.show("该名称在其他任务中已经存在,请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			nameTextbox.focus();
			return;
		}

			Task t = new Task();
			TaskPack tp = new TaskPack();
			t.setType("3");
			t.setName(nameTextbox.getValue().trim());//bug
			t.setDescription(description.getValue());

			t.setStart0(getTimeString(m0,m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12,m13,m14,m15,m16,m17,m18,m19,m20,m21,m22,m23));	
			t.setStart1(getTimeString(t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19,t20,t21,t22,t23));
			t.setStart2(getTimeString(w0,w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11,w12,w13,w14,w15,w16,w17,w18,w19,w20,w21,w22,w23));
			t.setStart3(getTimeString(s0,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19,s20,s21,s22,s23));
			t.setStart4(getTimeString(f0,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15,f16,f17,f18,f19,f20,f21,f22,f23));
			t.setStart5(getTimeString(sa0,sa1,sa2,sa3,sa4,sa5,sa6,sa7,sa8,sa9,sa10,sa11,sa12,sa13,sa14,sa15,sa16,sa17,sa18,sa19,sa20,sa21,sa22,sa23));
			t.setStart6(getTimeString(su0,su1,su2,su3,su4,su5,su6,su7,su8,su9,su10,su11,su12,su13,su14,su15,su16,su17,su18,su19,su20,su21,su22,su23));
			
			tp.createTask(t);
	
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		if(editRelativeTask.getAttribute("flag").equals("edit"))//编辑部分
		{
			String minfo=loginname+" "+"在"+OpObjectId.relative_task.name+"中进行了  "+OpTypeId.edit.name+"操作, 编辑项为:"+nameTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.relative_task);	
			Session session = this.session;
			session.setAttribute(TaskConstant.edit_relative_section,nameTextboxValue);

		}else{	//添加											
			String minfo=loginname+" "+"在"+OpObjectId.relative_task.name+"中进行了  "+OpTypeId.add.name+"操作，添加项为:"+nameTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.relative_task);	
			Session session = this.session;
			session.setAttribute(TaskConstant.add_relative_section,nameTextboxValue);

		}	
		String targetUrl = "/main/setting/taskrelative.zul";
		
		eccBody = (Include) (this.desktop.getPage("eccmain")
				.getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
		editRelativeTask.detach();

	}
	
	public String getTimeString(Checkbox c0,Checkbox c1,Checkbox c2,Checkbox c3,Checkbox c4,Checkbox c5,Checkbox c6,Checkbox c7,
			Checkbox c8,Checkbox c9,Checkbox c10,Checkbox c11,Checkbox c12,Checkbox c13,Checkbox c14,Checkbox c15,Checkbox c16,Checkbox c17,
			Checkbox c18,Checkbox c19,Checkbox c20,Checkbox c21,Checkbox c22,Checkbox c23){
			
		String valueString="";
		valueString+=getString(c0);
		valueString+=getString(c1);
		valueString+=getString(c2);
		valueString+=getString(c3);
		valueString+=getString(c4);
		valueString+=getString(c5);
		valueString+=getString(c6);
		valueString+=getString(c7);
		valueString+=getString(c8);
		valueString+=getString(c9);
		valueString+=getString(c10);
		valueString+=getString(c11);
		valueString+=getString(c12);
		valueString+=getString(c13);
		valueString+=getString(c14);
		valueString+=getString(c15);
		valueString+=getString(c16);
		valueString+=getString(c17);
		valueString+=getString(c18);
		valueString+=getString(c19);
		valueString+=getString(c20);
		valueString+=getString(c21);
		valueString+=getString(c22);
		valueString+=getString(c23);
		return valueString;
	}
	
	public String getString(Checkbox cb){
		String timeString="";
		if(cb.isChecked()){
			timeString=timeString+1;
		}else{
			timeString=timeString+0;
		}
		return timeString;
	}
}
