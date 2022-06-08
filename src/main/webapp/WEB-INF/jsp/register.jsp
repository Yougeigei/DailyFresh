<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html lang="zh_CN">
<head>
  <meta charset="UTF-8">
  <title>天天生鲜-注册</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/reset.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
  <script src="${pageContext.request.contextPath}/js/jquery-1.12.4.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/register.js"></script>
  <script>

      $(function () {

          $.get("${pageContext.request.contextPath}/footer", function (data) {
              $("#footer").html(data);
          });

          $('#user_name').blur(check_user_name);
          $('#pwd').blur(check_pwd);
          $('#cpwd').blur(check_cpwd);
          $('#email').blur(check_email);

          var check_flag = true;
          $('#allow').click(function () {
              check_flag = $(this).is(':checked');
              if (check_flag) {
                  $(this).siblings('span').hide();
              }
              else {
                  $(this).siblings('span').show();
              }
          });

          $('#registerForm').submit(function () {
              // 表单提交时进行校验
              if (check_user_name() && check_pwd() && check_cpwd() && check_email() && check_flag) {
                  // 校验通过，发送ajax请求
                  var username = $("#user_name").val();
                  var pwd = $("#pwd").val();
                  var email = $("#email").val();
                  // 校验通过，发送ajax请求，提交表单数据
                  $.ajax({
                      // 请求路径
                      url: "${pageContext.request.contextPath}/user/registerHandle",
                      // 请求类型
                      type: "post",
                      // 发送json数据
                      data: JSON.stringify({username: username, password: pwd, email: email}),
                      // 请求格式为Json
                      contentType: "appilcation/json;charset=utf-8",
                      // 定义回调响应的格式为json
                      dataType: "json",
                      success: function (data) {
                          // 处理返回数据，格式为{flag:true,massage:""}
                          if(data.flag) {
                              // 注册成功，跳转页面
                              location.href="${pageContext.request.contextPath}/registOk";
                          } else {
                              // 注册失败
                              $("#pop_msg").text(data.message);
                              $('.popup_con').fadeIn('fast');
                          }
                      }
                  });
              }
              // 不让表单进行提交，自己处理
              return false;
          });

          $(document).click(function(){
              $('.popup_con').fadeOut();
          });
      });

  </script>

</head>
<body>

<div class="register_con">
  <div class="l_con fl">
    <a class="reg_logo"><img src="images/logo02.png"></a>
    <div class="reg_slogan">足不出户 · 新鲜每一天</div>
    <div class="reg_banner"></div>
  </div>

  <div class="r_con fr">
    <div class="reg_title clearfix">
      <h1>用户注册</h1>
      <a href="${pageContext.request.contextPath}/login">登录</a>
    </div>
    <div class="reg_form clearfix">
      <form id="registerForm">
        <ul>
          <li>
            <label>用户名:</label>
            <input type="text" name="user_name" id="user_name" value=""/>
            <span class="error_tip">用户名需输入5-20位</span>
          </li>
          <li>
            <label>密码:</label>
            <input type="password" name="pwd" id="pwd" value=""/>
            <span class="error_tip">密码5~20位</span>
          </li>
          <li>
            <label>确认密码:</label>
            <input type="password" name="cpwd" id="cpwd" value=""/>
            <span class="error_tip">两次密码不一致</span>
          </li>
          <li>
            <label>邮箱:</label>
            <input type="text" name="email" id="email" value=""/>
            <span class="error_tip">邮件格式不正确</span>
          </li>
          <li class="agreement">
            <input type="checkbox" name="allow" id="allow" checked="checked"/>
            <label>同意"天天生鲜用户使用协议"</label>
            <span class="error_tip2">请同意协议</span>
          </li>
          <li class="reg_sub">
            <input type="submit" value="注 册"/>
          </li>
        </ul>
      </form>
    </div>

  </div>

</div>

<div class="footer no-mp" id="footer"></div>

<div class="popup_con">
  <div class="popup">
    <p id="pop_msg"></p>
  </div>
  <div class="mask"></div>
</div>

</body>
</html>