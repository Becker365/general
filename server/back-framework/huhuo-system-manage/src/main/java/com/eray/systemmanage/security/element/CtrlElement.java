package com.eray.systemmanage.security.element;

import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huhuo.integration.base.BaseCtrl;
import com.huhuo.integration.db.mysql.Condition;
import com.huhuo.integration.util.CheckUtils;
import com.huhuo.integration.util.ExtUtils;
import com.huhuo.integration.web.Message;
import com.huhuo.integration.web.Message.Status;

@Controller("smElement")
@RequestMapping(value="/sm/security/element")
public class CtrlElement extends BaseCtrl {
	private String basePath = "system-manage/security/element/";
	
	@Resource(name="smServElement")
	private IServElement servElement;
	
	@RequestMapping(value="/index.do")
	public String index(){
		return basePath + "index";
	}
	
	@RequestMapping(value="/get.do")
	public void get(ModelElement element, Condition<ModelElement> condition, OutputStream out){
		logger.debug("server receive: element={}, condition={}", element, condition);
		try{
			condition.setT(element);
			List<ModelElement> records = servElement.findByCondition(condition);
			Long count = servElement.countByCondition(condition);
			write(ExtUtils.getJsonStore(records, count), out);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			write(new Message<String>(Status.ERROR, e.getMessage()), out);
		}
	}
	
	@RequestMapping(value="/save.do")
	public void save(ModelElement element, OutputStream out){
		logger.debug("server receive: element={}", element);
		Message<String> r = null;
		try{
			if(element.getId()==null){	// add
				r =valid(element);
				if(r.getStatus() == Status.SUCCESS){
					int row = servElement.add(element);
					if(row>0){
						r = new Message<String>(Status.SUCCESS, "添加成功");
					}else{
						r = new Message<String>(Status.FAILURE, "添加失败");
					}
				}
			}else{						//	update
				r =valid(element);
				if(r.getStatus() == Status.SUCCESS){
					int row = servElement.update(element);
					if(row>0){
						r = new Message<String>(Status.SUCCESS, "更新成功");
					}else{
						r = new Message<String>(Status.FAILURE, "更新失败");
					}
				}
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			r = new Message<String>(Status.ERROR, e.getMessage());
		}
		write(r, out);
	}
	
	@RequestMapping(value="/delete.do")
	public void delete(ModelElement element, OutputStream out){
		logger.debug("server receive: element={}", element);
		Message<String> r = null;
		try{
			int row = servElement.delete(element);
			if(row>0){
				r = new Message<String>(Status.SUCCESS, "删除成功");
			}else{
				r = new Message<String>(Status.FAILURE, "删除失败");
			}
		}catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			r = new Message<String>(Status.ERROR, e.getMessage());
		}
		write(r, out);
	}
	
	protected Message<String> valid(ModelElement element){
		String msg = null;
		Status status = Status.FAILURE;
		if(!CheckUtils.validLength(1, 255, element.getName())){
			msg = "name字段长度应该在1-255个字符之间";
		}else if(!CheckUtils.validLength(1, 1000, element.getLocation())){
			msg = "location字段长度应该在1-1000个字符之间";
		}else{
			status = Status.SUCCESS;
		}
		return new Message<String>(status, msg);
	}
}
