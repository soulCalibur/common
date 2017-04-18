var canSearch=false;
$(function(){
	 if($("#searchKey").length>0){
		 canSearch=true;
	 }else {
		 window.location.href="index.htm";
	 }
	var apiKey=new RegExp("^.*/_guest/(register|login|refershToken|phoneLogin)((/|$))");
	var stopFindTokenKey=false;
	
	var UserEvent=function(i,value){
		if($(value).val().length<1){
			$(value).val(hhly.auth.userId);
			console.log('华海校体项目数据自动缓存默认赋值userId=', hhly.auth.userId);
		}
		$(value).change(function(){
			if($(value).val().length>0){
				hhly.auth.userId=$(value).val();
				BBH.cookies.setCookie("hhly_user",hhly.auth.userId,30);
				hhly.auth.token=BBH.cookies.getCookie("hhly_token_user_"+hhly.auth.userId);
				console.log('华海校体项目数据userId切换为', hhly.auth.userId+"对应token自动变更缓存"+hhly.auth.token);
			}
		});
		
	};
	
	var responseChange=function(){
		
		var json=$(this).text();
		var result=$.parseJSON(json);
		console.log('华海校体项目数据接口响应', result.msg);
		if(result.data){
			if(result.data.token){
				hhly.auth.token=result.data.token;
				hhly.auth.userId=result.data.userId;
				BBH.cookies.setCookie("hhly_token_user_"+hhly.auth.userId,hhly.auth.token,30);
				BBH.cookies.setCookie("hhly_user",hhly.auth.userId,30);
			console.log("华海校体项目半持久化用户"+hhly.auth.userId+"的token", hhly.auth.token);
			}
		}
	};	
	
	var submitEvent=function(){
		var flag=true;
		console.log('华海校体项目数据接口预置计算token鉴权',hhly.auth.token);
		var data={};
		data["X-SNS-UserId"]="";
		data["X-SNS-Signature"]="";
		data["X-SNS-Timestamp"]="";
		data["X-SNS-deviceToken"]="";
		data["X-SNS-AntToken"]="";
		if(hhly.auth.token){
			data["X-SNS-UserId"]=hhly.auth.userId;
			data["X-SNS-deviceToken"]=hhly.auth.token;
			data["X-SNS-AntToken"]=hhly.auth.antToken;
			var timestamp=new Date().getTime();
			var sign=hhly.getSign(hhly.auth.userId, timestamp, hhly.auth.token);
			data["X-SNS-Signature"]=sign;
			data["X-SNS-Timestamp"]=timestamp;
			console.log('华海校体项目数据接口预置计算鉴权结果',sign);
		}
		data["X-SNS-OrgId"]=null;
		data["X-SNS-OrgUserId"]=null;
		if($("#org_id").val().length>0){
			data["X-SNS-OrgId"]=$("#org_id").val();
			console.log('华海校体项目数据接口身份切换org_id',data["X-SNS-OrgId"]);
			hhly.shake();
		}
		if($("#role_id").val().length>0){
			data["X-SNS-RoleId"]=$("#role_id").val();
			console.log('华海校体项目数据接口身份切换X-SNS-RoleId ',data["X-SNS-RoleId"]);
			hhly.shake();
		}
		$.each(data, function (key, value) {
			if(!writeHeader(key,value)){
				flag=false;
				console.log('写入http头出错',key+"="+value);
				return;
			}else{
				console.log('写入http头成功',key+"="+value);
			}
		});
		
		return flag;
			
	};
	
	var apiKeyServices=function(api,apiView,self){
		var apiName=$(apiView).parent().text().trim();
		if( apiName.length>0 &&apiKey.test(apiName)){
			stopFindTokenKey=true;
			$(self.target).find("div[class='block response_body']").each(function(iresponse,responseView){
				BBH.dom.addChangeListening(responseView, responseChange);
			});
		}
	
	};
	
	var bodyChage=function(self) {
			$(self.target).find("form[class='sandbox']").each(function(i,formView){
			$(formView).find("input[class='submit']").click(submitEvent);
			$(formView).find("input[name='userId']").each(UserEvent);
			console.log('绑定提交前置处理事件==>>',submitEvent);
		});
	
		if(!stopFindTokenKey){
			$(self.target).find("a[class='toggleOperation ']").each(function(api,apiView){apiKeyServices(api,apiView,self);});
		}
		
		 if(canSearch){
			 if($("#searchKey").val().length>0){ //支持并为检索模式查询就展开
				 $(self.target).find("a[class='collapseResource']").click();
			 }
		}
	};
	
	function writeHeader(name,value){
		if(!value||value.length<1){
			 window.swaggerUi.api.clientAuthorizations.remove(name);
		}else {
			 var apiKeyAuth = new SwaggerClient.ApiKeyAuthorization(name, value, "header");
	         window.swaggerUi.api.clientAuthorizations.add(name, apiKeyAuth);
		}
		
         return true;
	}
	
	hhly.initMode();
	BBH.dom.addChangeListening("body", bodyChage);
	
	 if(canSearch){//搜索绑定回车触发
		 $("#searchKey").bind('keypress',function(event){
			 if(event.keyCode == "13"){
				 hhly.searchMode="all";
			 $("#explore").click(); 
			 stopFindTokenKey=false;
			 }
			 });
	};
	 if($("#searchTitle").length>0){
		 $("#searchTitle").click(function(){
			 hhly.searchMode="title";
			 $("#explore").click();
			 stopFindTokenKey=false;
		});
	 }
	
	
}); 

var hhly={
		getSign:function(userId,timestamp,token){
			var inStr = userId + token + timestamp;
			var sign = hex_sha1(inStr);
			return sign;
		},
		
		shake:function(){
		    var $panel = $("#pordAttr");
		    box_left =100;
		    $panel.css({'left': box_left,'position':'absolute'});
		    for(var i=1; 4>=i; i++){
		        $panel.animate({left:box_left-(40-10*i)},50);
		        $panel.animate({left:box_left+2*(40-10*i)},50);
		    }
		   
		    $panel.removeAttr("style");
		},
		
		initMode:function(){
			if(hhly.auth == undefined){
				hhly.auth={};
			
				if(BBH.cookies.getCookie("hhly_token_user_"+BBH.cookies.getCookie("hhly_user")).length>2){
					hhly.auth.userId=BBH.cookies.getCookie("hhly_user");
					hhly.auth.token=BBH.cookies.getCookie("hhly_token_user_"+hhly.auth.userId);
				console.log("华海校体项目半持久化用户"+hhly.auth.userId+"的token", hhly.auth.token);
				}
			}
			
		},
		managerData:function(responseObj){
			 if(canSearch){
				 var searchKey=$("#searchKey").val();
				 if(searchKey.length>0){
					console.log("查找"+searchKey);
					$.each(responseObj.paths,function(k,path){
						if("title"==hhly.searchMode){
							for (var key in path) {
							    var content=path[key].summary;
								if(content.indexOf(searchKey)<1){
									delete responseObj.paths[k];
								}
							}
						}else {
							var content=JSON.stringify(path);
							if(content.indexOf(searchKey)<1){
								delete responseObj.paths[k];
							}
						}
					});
				 }
			}
			 hhly.searchMode="default";
		}
};
