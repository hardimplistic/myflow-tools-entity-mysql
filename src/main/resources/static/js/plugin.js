if($.base64){
	$.base64.utf8encode = true;
	$.base64.utf8decode = true;
	if (!String.prototype.btoa) {
		String.prototype.btoa = function () {
			return $.base64.btoa(this);
		};
	}
	if (!String.prototype.atob) {
		String.prototype.atob = function () {
			return $.base64.atob(this);
		};
	}
}

String.prototype.startsWith = function(needle) {
	return this.indexOf(needle) === 0;
};

String.prototype.endsWith = function(suffix) {
	return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

function isIgnore(value){
	if (value == 'ignore:undefined' || value == 'ignore:null') {
		return true;
	}
	return false;
}

if (!String.prototype.format) {
	String.prototype.format = function() {
		var args = arguments;
		return this.replace(/{(\d+)}/g, function(match, number) {
			return typeof args[number] != 'undefined' ? args[number] : match;
		});
	};
}
if (!String.format) {
	String.format = function(format) {
		var args = Array.prototype.slice.call(arguments, 1);
		return format.replace(/{(\d+)}/g, function(match, number) {
			return typeof args[number] != 'undefined' ? args[number] : match;
		});
	};
}

String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi" : "g")), replaceWith);
	} else {
		return this.replace(reallyDo, replaceWith);
	}
}

if (!Date.prototype.format) {
	Date.prototype.format = function (fmt) {
		var o = {
			"M+": this.getMonth() + 1,
			"d+": this.getDate(),
			"h+": this.getHours(),
			"m+": this.getMinutes(),
			"s+": this.getSeconds(), 
			"q+": Math.floor((this.getMonth() + 3) / 3),
			"S": this.getMilliseconds()
		};
		if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
	};
}

if (!String.prototype.toInteger) {
	String.prototype.toInteger = function() {
		var args = arguments;
		var value = this.replace(/\s/g, '');
		if (value != '' && isNaN(value) == false) {
			return parseInt(value, 10);
		}
		return 0;
	};
}

if (!String.prototype.toFloat) {
	String.prototype.toFloat = function() {
		var args = arguments;
		var value = this.replace(/\s/g, '');
		if (value != '' && isNaN(value) == false) {
			return parseFloat(value, 10);
		}
		return 0.0;
	};
}

if (!String.prototype.toBoolean) {
	String.prototype.toBoolean = function() {
		var args = arguments;
		if (this == 'true') {
			return true;
		}
		return false;
	};
}


function objectLength(o) {
	var l = 0;
	for (var i in o) {
		l ++;
	}
	return l;
}

function stringToFloat(str) {
	if (!str) {
		return 0;
	}
	str = str.replaceAll(',', '');
	return new Number(str);
}

function cloneObject(o) {
	return $.parseJSON($.toJSON(o));
}

// Extend the default Number object with a formatMoney() method:
// usage: someVar.formatMoney(decimalPlaces, symbol, thousandsSeparator, decimalSeparator)
// defaults: (2, "$", ",", ".")
Number.prototype.formatMoney = function (places, symbol, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    symbol = symbol !== undefined ? symbol : "$";
    thousand = thousand || ",";
    decimal = decimal || ".";
    var number = this,
        negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};

function formatTotal(str) {
	if (!str) {
		return '<b>- 合 计 -</b>';
	}
	return '<b>' + str + '</b>';
}

function formatSubTotal(str) {
	if (!str) {
		return '<b>- 小 计 -</b>';
	}
	return '<b>' + str + '</b>';
}

//// 8 character ID (base=2)
//uuid(8, 2)  //  "01001010"
//// 8 character ID (base=10)
//uuid(8, 10) // "47473046"
//// 8 character ID (base=16)
//uuid(8, 16) // "098F4D35"
function uuid(len, radix) {
	var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
	var uuid = [],
		i;
	radix = radix || chars.length;
	if (len) {
		// Compact form
		for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
	} else {
		// rfc4122, version 4 form
		var r;
		// rfc4122 requires these characters
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		uuid[14] = '4';
		// Fill in random data.  At i==19 set the high bits of clock sequence as
		// per rfc4122, sec. 4.1.5
		for (i = 0; i < 36; i++) {
			if (!uuid[i]) {
				r = 0 | Math.random() * 16;
				uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
			}
		}
	}
	return uuid.join('');
}

function CreateID(prefix){
	if(!prefix) prefix = 'ID';
	return '{0}_{1}_{2}'.format(prefix, uuid(8, 16), Math.round( Math.random() * 100 ));
}

/**
 * 获得指定表单里的所有值
 * @param $form
 */
function fetchForm(formJqObj){
	var eles = formJqObj[0].elements;
	var data = {};
	$.each(eles, function(i, v){
		var target = $(v);
		var name = target.attr('name');
		if(name){
			
			if(target[0].tagName == 'SELECT'){
				data[name] = target.val();
				return;
			}

			var type = target.attr('type');
			if(target[0].tagName == 'TEXTAREA' || 'text,password,hidden'.indexOf(type = type.toLowerCase()) > -1){
				if (name.endsWith('[]')) {
					var cdata = data[name];
					if(!cdata){
						cdata = data[name] = [];
					}
					cdata.push(target.val());
				} else {
					var cdata = data[name];
					if (cdata) {
						cdata = data[name] = [cdata];
						cdata.push(target.val());
					} else {
						data[name] = target.val();
					}
				}
			}else if(type == 'radio'){
				if(target[0].checked){
					data[name] = target.val();
				}
			}else if(type == 'checkbox'){
				if(target[0].checked){
					var cdata = data[name];
					if(!cdata){
						cdata = data[name] = [];
					}
					cdata.push(target.val());
				}
			}
		}
	});
	return data;
}

// ******************************************************** //
// NetWork
// ******************************************************** //
function GetCall(url, data, callback) {
    if (data && !$.isFunction(data)) {
        $.get(url, data, function(response, status, xhr){
            if (typeof response == "string") {
                response = $.parseJSON(response);
            }
            if (callback) {
                callback(response, status, xhr);
            }
        });
    } else {
        callback = data;
        $.get(url, function(response, status, xhr){
            if (typeof response == "string") {
                response = $.parseJSON(response);
            }
            if (callback) {
                callback(response, status, xhr);
            }
        });
    }
}

function PostCall(url, data, callback) {
    $.post(url, data, function(response, status, xhr){
        if (typeof response == "string") {
            response = $.parseJSON(response);
        }
        if (callback) {
            callback(response, status, xhr);
        }
    });
}

function JsonCall(url, data, callback) {
	$.ajax({
		type: "POST",
		url: url,
		data: $.toJSON(data),
		dataType:"json",
		contentType : 'application/json;charset=utf-8',
		success: function(data){
			callback(data);
		}
	});
}

function SerializeJsonCall(url, data, callback) {
	$.ajax({
		type: "POST",
		url: url,
		data: SerializeQueryString(data),
		dataType:"json",
		success: function(data){
			callback(data);
		}
	});
}

function SyncJsonCall(url, data, callback) {
	$.ajax({
		async: false,
		type: "POST",
		url: url,
		data: $.toJSON(data),
		dataType:"json",
		contentType : 'application/json;charset=utf-8',
		success: function(data){
			callback(data);
		}
	});
}

function SimpleRequest(data) {
	if (data) {
		return data;
	}
	return {};
}

function CreateRequest(data) {
	var oRequest = new Object();
	oRequest.ReqUUID = uuid(32, 62);
	oRequest.ReqTime = new Date().getTime();
	if (data) {
		oRequest.data = data;
	}
	return oRequest;
}

/**
 * 任务队列
 */
function Queue(){
	var self = this;
	self.index = 0;
	self.queue = [];
	self.reset = function(){
		self.index = 0;
	};
	self.clear = function(){
		self.queue = [];
		self.index = 0;
	};
	self.action = function(fn){
		self.queue.push(fn);
	};
	self.start = function(){
		if(self.queue.length == 0){
			return;
		}
		var next = function(data){
			var fn = self.queue[++self.index];
			if(fn){
				fn(next, data);
			}
		};
		self.queue[0](next);
	};
	return self;
}

//==========================================================
//HashCode BEGIN
//==========================================================
function isNull(str) {
    return str == null || str.value == "";
}

/** 
* java String hashCode 的实现 
* @param strKey 
* @return intValue 
*/
function hashCode(strKey) {
	var hash = 0;
	if (!isNull(strKey)) {
	    for (var i = 0; i < strKey.length; i++) {
	        hash = hash * 31 + strKey.charCodeAt(i);
	        hash = intValue(hash);
	    }
	}
	return hash;
}

/** 
* 将js页面的number类型转换为java的int类型 
* @param num 
* @return intValue 
*/
function intValue(num) {
	var MAX_VALUE = 0x7fffffff;
	var MIN_VALUE = -0x80000000;
	if (num > MAX_VALUE || num < MIN_VALUE) {
	    return num &= 0xFFFFFFFF;
	}
	return num;
}
//==========================================================
//HashCode END
//==========================================================


//==========================================================
// File Download BEGIN
//==========================================================

var __FILE_DOWNLOAD_FORM__ = CreateID();
var __FILE_DOWNLOAD_IFRAME__ = CreateID();
function FileDownloadCall(url, data, _target) {
	var dlForm = $('#' + __FILE_DOWNLOAD_FORM__ + _target);
	if (dlForm.size() == 0) {
		var body = $('body');
		body.append(
			$('<form autocomplete="on"/>').attr({
				id:     __FILE_DOWNLOAD_FORM__ + _target,
				action: url,
				method: 'POST',
				target: __FILE_DOWNLOAD_IFRAME__ + _target
			}).hide()
		).append(
			// $('<iframe/>').attr({
			// 	name: __FILE_DOWNLOAD_IFRAME__ + _target
			// }).hide()
		);
		dlForm = $('#' + __FILE_DOWNLOAD_FORM__ + _target);
	}

	dlForm.empty();

	for (var i in data) {
		var val = data[i];

		if ($.isArray(val)) {

			for (var j in val) {
				var v = val[j];
				var hidden = $('<input type="hidden">').attr('name', i);
				hidden.val(v);
				hidden.appendTo(dlForm);
			}

		} else {
			var hidden = $('<input type="hidden">').attr('name', i);
			hidden.val(val);
			hidden.appendTo(dlForm);
		}

	}

	dlForm.submit();
}

//==========================================================
// File Download END
//==========================================================

function dateInterval(start, end, type) {
	var interval = end.getTime() - start.getTime();
	interval = Math.abs(interval);
	switch(type) {
	case 'd':
		return Math.floor(interval / (24 * 3600 * 1000));
	case 'h':
		var leave1 = date3 % (24 * 3600 * 1000);
		var hours = Math.floor(leave1 / (3600 * 1000));
		var leave2 = leave1 % (3600 * 1000);
		var minutes = Math.floor(leave2 / (60 * 1000));
		return minutes;
	case 's':
		var leave3 = leave2 % (60 * 1000);
		var seconds = Math.round(leave3 / 1000);
		return seconds;
	}
	return null;
}

function dateCreate(date, type) {
	var _date = date ? date : new Date();

	switch(type) {
	case 'y':
		return _date = new Date(_date.format('yyyy') + '-01-01 00:00:00');
	case 'M':
		return _date = new Date(_date.format('yyyy-MM') + '-01 00:00:00');
	case 'd':
		return _date = new Date(_date.format('yyyy-MM-dd') + ' 00:00:00');
	case 'h':
		return _date = new Date(_date.format('yyyy-MM-dd hh') + ':00:00');
	case 'm':
		return _date = new Date(_date.format('yyyy-MM-dd hh:mm') + ':00');
	}

	return _date;
}

// 获取对象中多个层级的值
function depthValue(dataObject, depthKey) {

	var _depthKey = null;

	if (typeof depthKey == 'string') {
		_depthKey = depthKey.split('.');
	} else {
		_depthKey = depthKey;
	}

	var current = dataObject;

	var keyCount = _depthKey.length;

	var index = 0;
	for (var i = 0; i < keyCount; i++) {
		if (typeof current != 'object') {
			break;
		}
		var key = _depthKey.shift();
		current = current[key];
		if (current == undefined) {
			return null;
		}
	}

	return current;
}

function CreateSortRequest(condition, sortable, sortColumns, sortOrders) {

	sortColumns = $.isArray(sortColumns) ? sortColumns.join(',') : sortColumns;
	sortOrders = $.isArray(sortOrders) ? sortOrders.join(',') : sortOrders;

	var sortCondition = {
		sortable: (sortable == true ? true : false),
		sortColumns: sortColumns ? sortColumns : '',
		sortOrders: sortOrders ? sortOrders : ''
	};

	return $.extend(sortCondition, condition);
}

function JsonClone(obj) {
    if (!obj) {
        return null;
    }
    var serialize = JSON.stringify(obj);
    return $.parseJSON(serialize);
}

function GetSearchParam(key) {
    if (location.search && location.search.substring(1)) {
        var arr, reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
        if (arr = location.search.substring(1).match(reg))
            return unescape(arr[2]);
        else
            return null;
    } else {
        return null;
    }
}

// 仅支持单级序列化
function SerializeQueryString(object, _key) {
	if (!object || typeof object != 'object') {
		return object;
	}
	var chain = [];

	if ($.isArray(object)) {
		for (var i in object) {
			var o = object[i];
			if (typeof o != 'object') {
				chain.push(_key + '=' + o);
			} else {
				chain.push(SerializeQueryString(o, _key));
			}
		}
	} else {
		for (var i in object) {
			var o = object[i];
			if (typeof o != 'object') {
				chain.push(i + '=' + o);
			} else {
				chain.push(SerializeQueryString(o, i));
			}
		}
	}

	return chain.join('&');
}


function SerializeSearchString(object) {
	if (!object || typeof object != 'object') {
		return object;
	}
	var string = '';

	var index = 0;
	for (var i in object) {
		if (index == 0) {
			string += i + '=' + object[i];
		} else {
			string += '&' + i + '=' + object[i];
		}
		index ++;
	}

	return string;
}

function LocationRedirect(baseUrl, param) {
	if (param) {
		if (baseUrl.indexOf('?') == -1 && param) {
			baseUrl += '?';
		}
		baseUrl += SerializeSearchString(param);
	}
	location.href = baseUrl;
}

function previewPDF(file, filename, printNow) {
	var url = '/sjtu/ireport/preview';
	url += '?' + SerializeSearchString({
		file: encodeURIComponent(file),
		filename: encodeURIComponent(filename)
	});

	if (printNow == true) {
		url += '&printNow=true'
	}

	window.open(url, CreateID());
}



function arrayObjectColumnSumToNumber(arr, column) {
	if (!arr || arr.length == 0) {
		return 0;
	}
	var sum = 0;
	for (var i in arr) {
		var o = arr[i];
		var v = depthValue(o, column);
		if (v == undefined || typeof v == 'object') {
			continue;
		}
		if (typeof v == 'string') {
			sum += v.toFloat();
			continue;
		}
		if (typeof v == 'number') {
			sum += v;
			continue;
		}
	}
	return sum;
}

function arrayObjectColumnSumToString(arr, column, separator) {
	if (!arr || arr.length == 0) {
		return '';
	}
	separator = separator ? separator : ', ';
	var sum = '';
	for (var i in arr) {
		var o = arr[i];
		var v = depthValue(o, column);
		if (v == undefined || typeof v == 'object') {
			continue;
		}
		if (sum.length > 0) {
			sum += separator;
		}
		sum += v;
	}
	return sum;
}

function arrayObjectColumnNumberValueSubArray(arr, column, startValue, endValue) {
	if (!arr || arr.length == 0) {
		return [];
	}
	var _arr = [];
	for (var i in arr) {
		var o = arr[i];
		var v = depthValue(o, column);
		if (v == undefined || typeof v == 'object') {
			continue;
		}
		if (v >= startValue && v <= endValue) {
			_arr.push(o);
		}
	}
	return _arr;
}


// params ['column1', 'column2', 'column3'], [1, 2], [1, 2], [1, 2]
// return [{column1: 1, column2: 1, column3: 1}, {column1: 2, column2: 2, column3: 2}]
function arraysMergeToArrayObject() {
	var args = arguments;

	if (args < 2) {
		return [];
	}

	var _ArrObject = [];

	var columnNameArray = args[0];
	var rowIndex = args[1];

	for (var i = 0; i < rowIndex.length; i++) {
		var _Object = {};

		for (var n in columnNameArray) {
			var column = columnNameArray[n];
			var value  = args[n.toInteger() + 1][i];
			_Object[column] = value;
		}

		_ArrObject.push(_Object);
	}

	return _ArrObject;
}


function arrayObjectAppendColumn(arr, column, value) {
	for (var i in arr) {
		arr[i][column] = value;
	}
	return arr;
}

function arrayObjectColumnValue(arr, column) {
	var list = [];
	for (var i in arr) {
		list.push(arr[i][column]);
	}
	return list;
}

function arrayObjectToMapByColumn(arrObj, column) {
	var map = {};
	for (var i in arrObj) {
		map[arrObj[i][column]] = arrObj[i];
	}
	return map;
}