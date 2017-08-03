$(function(){
	// 表单校验
	$('form').bootstrapValidator({
		message: 'This value is not valid',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			enterprise: {
				validators: {
					notEmpty: {
						message: '请选择供应商!'
					}
				}
			},
			business_type: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			area: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			rmb: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
	                    regexp: /^[0-9]+(\.[0-9]{1,})?$/,
	                    message: '请输入正确的金额'
	                }
				}
			},
			usd: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
	                    regexp: /^[0-9]+(\.[0-9]{1,})?$/,
	                    message: '请输入正确的金额'
	                }
				}
			},
			eur: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
	                    regexp: /^[0-9]+(\.[0-9]{1,})?$/,
	                    message: '请输入正确的金额'
	                }
				}
			},
		}
	});
});