function recallCookie(url,type,data){ <!--엑세스 쿠키가 만료됐을 때 서버에 요청하는 함수-->
	  console.log("쿠키 재 요 청");
	   $.ajax({
	   url: "/api/token",
	   type:"POST",
	   success: function(result){
	   $.ajax({
				url : url,
				type : type,
				dataType : "json",
				data : data,
				contentType : "application/json",
				cache   : false,
				success : function(result, status){
				alert(result);
				},
				error: function(jqXHR, status, error){
				alert(jqXHR.responseText);
				}
				});
	   },
	   error: function(jqXHR, status, error){
	   alert("로그인 후 이용가능합니다.");
	   }
	   });
	 }


	  function logout(){
                console.log("logout");
                $.ajax({
                type : 'POST',
                url : '/logout',
                success : function(result){
                location.href="/";
                },
                error : function(jqXHR, status, error){
                console.log(jqXHR.responseText);
                }
                });
                }