/** index.js By Beginner Emain:zheng_jinfan@126.com HomePage:http://www.zhengjinfan.cn */
layui.config({
	base: '../../../resource/layui-admin/js/'
}).use(['element', 'layer', 'tab'], function() {
	var element = layui.element(),
		$ = layui.jquery,
		layer = layui.layer,
		
		tab = layui.tab({
			elem: '.admin-nav-card' //设置选项卡容器
		});
	//iframe自适应
	$(window).on('resize', function() {
		var $content = $('.admin-nav-card .layui-tab-content');
		$content.height($(this).height() - 147);
		$content.find('iframe').each(function() {
			$(this).height($content.height());
		});
	}).resize();


	/*//监听点击事件
	navbar.on('click(side)', function(data) {
		tab.tabAdd(data.field);
	});*/
	
	$('.site-demo-active2').on('click', function(data){
		if($(this).attr('_href')){
			var bStop = false;
			var bStopIndex = 0;
		    var othis = $(this), type = othis.data('type');
		    var _href=$(this).attr('_href');
			var _titleName=$(this).attr("data-title");
			
			tab.tabAdd(data.field);
			
			/*element.tabAdd('demo', {
			      title: _titleName //用于演示
			      ,content: '<iframe src="'+ _href +'"></iframe>'
			      ,id: new Date().getTime() //实际使用一般是规定好的id，这里以时间戳模拟下
			})*/
		}
	});
});