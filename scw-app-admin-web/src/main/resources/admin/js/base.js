$(function() {
	$("div.upload-img .add-img").click(function() {
		$(this).parent().find("input").click();
	})

	$("div.upload-img").each(function() {
		var isAutoAdd = $(this).attr("data-auto-add");
		if (isAutoAdd) {
			$(this).find(".delete-img").show();
		}
	})

	$("div.upload-img .delete-img").click(function() {
		var src = $(this).parent().find(".show-img").attr("src");
		if (isNull(src)) {
			var next = $(this).parent().next("div.upload-img");
			if (next.length != 0) {
				$(this).parent().remove();
			}
			return;
		} else {
			$(this).parent().find('input').val('');
			var showImg = $(this).parent().find('.show-img');
			showImg.removeAttr("src");
			// IE9以下
			showImg.css("filter", "");
			showImg.hide();
			
			$(this).parent().find('.add-img').show();

			var isAutoAdd = $(this).parent().attr("data-auto-add");
			if (!isAutoAdd) {
				$(this).hide();
			}
		}

		$(this).parent().find('input').removeAttr("data-url");
	})

	$("input[type='file']").change(function() {
		$(this).removeAttr("data-url");
	})

	$("div.upload-img input[type='file']")
			.change(
					function() {
						var file = $(this)[0];
						// 预览
						var pic = $(this).parent().find(".show-img");
						// 添加按钮
						var addImg = $(this).parent().find(".add-img");
						// 删除按钮
						var deleteImg = $(this).parent().find(".delete-img");

						var ext = file.value.substring(
								file.value.lastIndexOf(".") + 1).toLowerCase();

						// gif在IE浏览器暂时无法显示
						if (ext != 'png' && ext != 'jpg' && ext != 'jpeg'
								&& ext != "gif") {
							if (ext != '') {
								alert("图片的格式必须为png、jpg、jpeg、gif");
							}
							return;
						}
						// 判断IE版本
						var isIE = navigator.userAgent.match(/MSIE/) != null, isIE6 = navigator.userAgent
								.match(/MSIE 6.0/) != null;
						isIE10 = navigator.userAgent.match(/MSIE 10.0/) != null;
						if (isIE && !isIE10) {
							file.select();
							var reallocalpath = document.selection
									.createRange().text;
							// IE6浏览器设置img的src为本地路径可以直接显示图片
							if (isIE6) {
								pic.attr("src", reallocalpath);
							} else {
								// 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
								pic
										.css(
												"filter",
												"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src=\""
														+ reallocalpath + "\")");
								// 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
								pic
										.attr(
												'src',
												'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==');
							}
						} else {
							var file = file.files[0];
							var reader = new FileReader();
							reader.readAsDataURL(file);
							reader.onload = function(e) {
								pic.attr("src", this.result);
							}
						}
						addImg.hide();
						deleteImg.show();
						pic.show();
						var isAutoAdd = $(this).parent().attr("data-auto-add");
						if (isAutoAdd) {
							var uploadDiv = $(this).parent();
							if (uploadDiv.next(".upload-img").length == 0) {
								var clone = uploadDiv.clone(true);
								clone.find('input').val('');
								clone.find('input').attr("data-url", "");
								clone.find('.show-img').attr("src", "");
								// IE9以下
								clone.find('.show-img').css("filter", "");
								clone.find('.add-img').show();
								uploadDiv.parent().append(clone);
							}
						}
					})
})

function isNull() {
	if (arguments) {
		for (var i = 0; i < arguments.length; i++) {
			var v = arguments[i];
			if (!v || v == null || v == undefined || "undefined" == typeof (v)
					|| v.length == 0) {
				return true;
			}
		}
	}
	return false;
}

function fileInputIsNull(fileInput) {
	var b = false;
	$(fileInput).each(function() {
		console.log("before:" + b);
		if (b) {
			return false;
		}

		b = isNull($(this).val());
		if (b) {
			b = isNull($(this).attr("data-url"));
		}

		console.log("after:" + b);
		if (b) {
			return false;
		}
	})
	return b;
}

function fileInputNotNull(fileInput) {
	var b = true;
	$(fileInput).each(function() {
		if (!b) {
			return false;
		}

		b = isNull($(this).val());
		if (b) {
			b = isNull($(this).attr("data-url"));
		}

		if (!b) {
			return false;
		}
	})
	return !b;
}

function previewImg(input, pic) {
	var file = $(input)[0];
	if (!file || !file.value || file.value.length == 0) {
		return;
	}

	var lastIndex = file.value.lastIndexOf(".");
	if (lastIndex == -1) {
		return;
	}

	if ($(pic).length == 0) {
		return;
	}

	var tagName = $(pic).prop("tagName").toLowerCase();
	var ext = file.value.substring(lastIndex + 1).toLowerCase();
	// gif在IE浏览器暂时无法显示
	if (ext != 'png' && ext != 'jpg' && ext != 'jpeg' && ext != "gif") {
		if (ext != '') {
			alert("图片的格式必须为png、jpg、jpeg、gif");
		}
		return;
	}

	// 判断IE版本
	var isIE = navigator.userAgent.match(/MSIE/) != null, isIE6 = navigator.userAgent
			.match(/MSIE 6.0/) != null;
	isIE10 = navigator.userAgent.match(/MSIE 10.0/) != null;
	if (isIE && !isIE10) {
		file.select();
		var reallocalpath = document.selection.createRange().text;
		// IE6浏览器设置img的src为本地路径可以直接显示图片
		if (isIE6) {
			if (tagName == "img") {
				$(pic).attr("src", reallocalpath);
			} else {
				var img = document.createElement("img");
				$(img).attr("src", reallocalpath);
				$(pic).html(img);
			}
		} else {
			// 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
			if (tagName == "img") {
				$(pic)
						.css(
								"filter",
								"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src=\""
										+ reallocalpath + "\")");
				// 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
				$(pic)
						.attr(
								'src',
								'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==');
			} else {
				var img = document.createElement("img");
				$(img)
						.css(
								"filter",
								"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src=\""
										+ reallocalpath + "\")");
				// 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
				$(img)
						.attr(
								'src',
								'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==');
				$(pic).html(img);
			}
		}
	} else {
		var file = file.files[0];
		var reader = new FileReader();
		reader.readAsDataURL(file);
		reader.onload = function(e) {
			if (tagName == "img") {
				$(pic).attr("src", this.result);
			} else {
				var img = document.createElement("img");
				$(img).attr("src", this.result);
				$(pic).html(img);
			}
		}
	}
}


function clearFileInputDataUrl(fileInput){
	$(fileInput).attr("data-url", "");
}

function uploadImg(url, fileInput, successCall) {
	var dataUrl = $(fileInput).attr("data-url");
	if (dataUrl && dataUrl.length > 0) {
		successCall(dataUrl);
	} else {
		$.ajaxFileUpload({
			url : url, //用于文件上传的服务器端请求地址
			secureuri : false, //一般设置为false
			fileInput : "#" + $(fileInput).attr("id"), //文件上传空间的id属性  <input type="file" id="file" name="file" />
			dataType : 'JSON', //返回值类型 一般设置为json
			success : function(data, status) //服务器成功响应处理函数
			{
				var json = JSON.parse(data);
				if (json.error == 0) {
					var uri = json.data;
					$(fileInput).attr("data-url", uri);
					successCall(uri);
				} else {
					alert(json.data);
				}
			},
			error : function(data, status, e)//服务器响应失败处理函数
			{
				console.log(data);
				console.log(e);
				alert("系统错误");
			}
		})
	}
}

function Base64(inputStr) {
	if (!inputStr) {
		throw "参数不能为空";
	}

	this.utf8_encode = function(str) {
		str = str.replace(/\r\n/g, "\n");
		var utftext = "";
		for (var n = 0; n < str.length; n++) {
			var c = str.charCodeAt(n);
			if (c < 128) {
				utftext += String.fromCharCode(c);
			} else if ((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			} else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}

		}
		return utftext;
	}

	this.utf8_decode = function(utftext) {
		var string = "", i = 0, c = 0, c1 = 0, c2 = 0, c3 = 0;
		while (i < utftext.length) {
			c = utftext.charCodeAt(i);
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			} else if ((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i + 1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			} else {
				c2 = utftext.charCodeAt(i + 1);
				c3 = utftext.charCodeAt(i + 2);
				string += String.fromCharCode(((c & 15) << 12)
						| ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
		}
		return string;
	}

	this._keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
	this.encode = function() {
		var output = "", chr1, chr2, chr3, enc1, enc2, enc3, enc4, i = 0;
		input = this.utf8_encode(inputStr);
		while (i < inputStr.length) {
			chr1 = inputStr.charCodeAt(i++);
			chr2 = inputStr.charCodeAt(i++);
			chr3 = inputStr.charCodeAt(i++);
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}
			output = output + this._keyStr.charAt(enc1)
					+ this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3)
					+ this._keyStr.charAt(enc4);
		}
		return output;
	}

	this.decode = function() {
		var output = "", chr1, chr2, chr3, enc1, enc2, enc3, enc4, i = 0;
		inputStr = inputStr.replace(/[^A-Za-z0-9\+\/\=]/g, "");
		while (i < inputStr.length) {
			enc1 = this._keyStr.indexOf(inputStr.charAt(i++));
			enc2 = this._keyStr.indexOf(inputStr.charAt(i++));
			enc3 = this._keyStr.indexOf(inputStr.charAt(i++));
			enc4 = this._keyStr.indexOf(inputStr.charAt(i++));
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
			output = output + String.fromCharCode(chr1);
			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}
		}
		output = this.utf8_decode(output);
		return output;
	}
}