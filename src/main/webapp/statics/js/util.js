/**
 * 判断字符串是否为空
 * 	为空，返回true
 * 	不为空，返回false
 * @param str
 * @returns {Boolean}
 */
// 要调用这个方法需要在jsp中引用
function isEmpty(str) {
	if (str == null || str.trim() == "") {
		return true;
	}
	return false;
}