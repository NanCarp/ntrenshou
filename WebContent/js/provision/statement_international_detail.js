
$(function(){
	// 日期控件
	$(".form_datetime").datetimepicker({
		format: "yyyy-mm-dd",
		autoclose: true,
		todayBtn: true,
		todayHighlight: true,
		showMeridian: true,
		pickerPosition: "bottom-left",
		language: 'zh-CN',//中文，需要引用zh-CN.js包
		startView: 2,//月视图
		minView: 2//日期时间选择器所能够提供的最精确的时间选择视图
	});
	// 选择日期后自动触发校验的代码
	// TODO
	
	// 表单校验
	$('form').bootstrapValidator({
		message: 'This value is not valid',
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			my_company_no: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			company_no: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			sell: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			sell_area: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
                        regexp: /^[a-zA-Z0-9_\u0391-\uFFE5]+$/,
                        message: '不能包含特殊字符'
                    }
				}
			},
			dest_port: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
                        regexp: /^[a-zA-Z0-9_\u0391-\uFFE5]+$/,
                        message: '不能包含特殊字符'
                    }
				}
			},
			trade_clause: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			transport: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			enterprise: {
				validators: {
					notEmpty: {
						message: '请选择货代!'
					}
				}
			},
			quantity: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
			            regexp: /^[0-9]+$/,
			            message: '请输入正确的数量'
			        },
				}
			},
			weight: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
	                    regexp: /^[0-9]+(\.[0-9]{1,4})?$/,
	                    message: '请输入正确的重量'
	                }
				}
			},
			volume: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
                        regexp: /^[0-9]+(\.[0-9]{1,4})?$/,
                        message: '请输入正确的体积'
                    }
				}
			},
			container_quantity: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
			            regexp: /^[0-9]+$/,
			            message: '请输入正确的数量'
			        },
				}
			},
			container_type: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
                        regexp: /^[a-zA-Z0-9_\u0391-\uFFE5]+$/,
                        message: '不能包含特殊字符'
                    }
				}
			},
			container_no: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					},
					regexp: {
	                    regexp: /^[a-zA-Z0-9_\u0391-\uFFE5]+$/,
	                    message: '不能包含特殊字符'
	                }
				}
			
			},
			factory_date: {
				validators: {
					notEmpty: {
						message: '请选择日期!'
					}
				}
			},
			airline: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			flight_name: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			flight_no: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			bl_no: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			estimated_departure_date: {
				validators: {
					notEmpty: {
						message: '请选择日期!'
					}
				}
			},
			actual_departure_date: {
				validators: {
					notEmpty: {
						message: '请选择日期!'
					}
				}
			},
			estimated_arrival_date: {
				validators: {
					notEmpty: {
						message: '请选择日期!'
					}
				}
			},
			actual_arrival_date: {
				validators: {
					notEmpty: {
						message: '请选择日期!'
					}
				}
			},
			customs_fee: {
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
			customs_fee_remark: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			domestic_trailer_fee: {
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
			domestic_trailer_fee_remark: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			ocean_freight: {
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
			ocean_freight_remark: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			domestic_agent: {
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
			domestic_agent_remark: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			customs_clearance_fee: {
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
			customs_clearance_fee_remark: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			foreign_delivery_fee: {
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
			foreign_delivery_fee_remark: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			international_agent: {
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
			international_agent_remark: {
				validators: {
					notEmpty: {
						message: '不能为空!'
					}
				}
			},
			overnight_fee: {
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
			empty_fee: {
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
			handling_fee: {
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
			inspection_fee: {
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