package app.cmapp.WebAPIClient;/**
 * Created by F5460007 on 2017/3/31.
 */

/**
 * Owner:F5460007
 * CreateDate:2017/3/31 14:06
 */
/// <summary>
/// 返回結果類型
/// </summary>
public class webapivalue {
    /// <summary>
    /// 回饋結果狀態 1:成功;0:失敗;
    /// </summary>
    public String status;
    //        public void
    public String msg;
    //        public  void  setmsg(String v) {
//            msg = v;
//        }
//        public String get_msg() {
//            return msg;
//        }
    /// <summary>
    /// 用戶自定義返回識別碼
    /// </summary>
    public String requestname;
    //        public void set_requestname(String v) {
    //            requestname = v;
    //        }
    //        public String get_requestname() {
    //            return requestname;
    //        }

        /// <summary>
        /// 返回DataTable
        /// </summary>
    public Object rvalue;
//        public void setrvalue(Object o) {
//            rvalue = o;
//        }
//        public Object get_rvalue() {
//            return rvalue;
//        }
}
