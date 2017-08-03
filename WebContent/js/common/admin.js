function layer_show(title,url,w,h){
	if (title == null || title == '') {
		title=false;
	};
	if (url == null || url == '') {
		url="404.html";
	};
	if (w == null || w == '') {
		w=800;
	};
	if (h == null || h == '') {
		h=($(window).height() - 50);
	};
	layer.open({
		maxmin:false,
		type: 2,
		area: [w+'px', h +'px'],
		fix: false, //不固定
		shade:0.4,
		title:[title,'font-weight:600;color:#fff;background-color: #3367D6;'],
		content: url
	});
}
function layer_show_noshadow(title,url,w,h){
	if (title == null || title == '') {
		title=false;
	};
	if (url == null || url == '') {
		url="404.html";
	};
	if (w == null || w == '') {
		w=800;
	};
	if (h == null || h == '') {
		h=($(window).height() - 50);
	};
	layer.open({
		type: 2,
		area: [w+'px', h +'px'],
		fix: false, //不固定
		maxmin: true,
		shade:0,
		title: title,
		content: url
	});
}
/*关闭弹出框口*/
function layer_close(){
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}
function open_window(title, url, w, h){
	layer_show(title, url, w, h);
}

function open_window_weixin(title, url, w, h){
	layer_show(title, url, w, h);
}
function open_window_noshadow(title, url, w, h){
	layer_show_noshadow(title, url, w, h);
}

function refresh_iframe(){
	var iframedoc = $('.layui-tab-item iframe:visible').get(0);
	iframedoc.src = iframedoc.src;
}
function go_iframe(){
	var iframedoc = $('.layui-tab-item iframe:visible').get(0);
	iframedoc.src = 'http://'+ window.location.host +'/statistic/companylistForSearch';
}
function open_schedule(url, w, h){
	layer.open({
		type: 2,
		area: [w+'px', h +'px'],
		fix: true, //不固定
		title: [false,'font-weight:600;color:#fff;background-color: #3367D6;'],
		closeBtn: 0,
		content: url
	});
}

//全屏询问框{
function actconfirm(title, next){
	layer.confirm(title, function(index){
		layer.close(index);
		next();
	});
}

//登录过期
function loginOut(){
	layer.confirm('登录过期，重新登录？',function(index){
		location.replace('/pages/login');
	});
}	

//多账号登录退出
function conflictOut(){
	layer.confirm('您的账号在别处登录，请重新登录？',function(index){
		location.replace('/pages/login');
	});
}

//乘法计算
function accMul(arg1,arg2)   
{   
    var m=0,s1=arg1.toString(),s2=arg2.toString();   
    try{m+=s1.split(".")[1].length}catch(e){}   
    try{m+=s2.split(".")[1].length}catch(e){}   
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)   
} 

//除法计算
function accDiv(arg1,arg2){   
    var t1=0,t2=0,r1,r2;   
    try{t1=arg1.toString().split(".")[1].length}catch(e){}   
    try{t2=arg2.toString().split(".")[1].length}catch(e){}   
    with(Math){   
    r1=Number(arg1.toString().replace(".",""))   
    r2=Number(arg2.toString().replace(".",""))   
    return (r1/r2)*pow(10,t2-t1);   
    }   
} 

//加法计算
function accAdd(arg1,arg2){      
	   var r1,r2,m;      
	   try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}    
	   try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}     
	        m=Math.pow(10,Math.max(r1,r2))  
	        return (arg1*m+arg2*m)/m      
	}  

//减法计算
function accSub(arg1,arg2){      
	 var r1,r2,m;      
	 try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}    
	 try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}     
	     m=Math.pow(10,Math.max(r1,r2))  
	     return (arg1*m-arg2*m)/m      
} 
