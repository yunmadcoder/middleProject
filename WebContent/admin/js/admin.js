function check_all(f)
{
    var chk = document.getElementsByName("chk[]");

    for(i=0; i<chk.length; i++)
        chk[i].checked = f.chkall.checked;
}

function is_checked(elements_name)
{
    var checked = false;
    var chk = document.getElementsByName(elements_name);
    for(var i=0; i<chk.length; i++) {
        if(chk[i].checked) {
            checked = true;
        }
    }
    return checked;
}

function delete_confirm2(msg)
{
    if(confirm(msg))
        return true;
    else
        return false;
}

function get_ajax_token()
{
    var token = "";

    $.ajax({
        type: "POST",
        url: tb_admin_url+"/ajax.token.php",
        cache: false,
        async: false,
        dataType: "json",
        success: function(data) {
            if(data.error) {
                alert(data.error);
                if(data.url)
                    document.location.href = data.url;

                return false;
            }

            token = data.token;
        }
    });

    return token;
}

$(function(){	
	// ajax 실행 및 완료시 'LOADING 이미지'의 동작을 컨트롤.
	$("#ajax-loading").ajaxStart(function() {
		$(this).show();
	})
	.ajaxStop(function() {
		$(this).delay(10).hide(1); 
	});

	// 검색폼 초기화
	$(document).on("click", "#frmRest", function() {
		$(":input","#fsearch")
		 .not(":submit, :reset, :hidden, :button")
		 .val("")
		 .removeAttr("checked")
		 .removeAttr("selected");
    });

	// 좌측메뉴 menu (close/open)
	$(document).on("click", ".menu_toggle", function() {
		var info = $(this).attr('class').split(' ');
		var menu = info[0];

		if($(this).is(".menu_close")) {
			$(this).removeClass("menu_close");
			$(this).nextAll('dd.'+menu).slideDown('fast');
			var gubun = '';
		} else {			
			$(this).addClass("menu_close");
			$(this).nextAll('dd.'+menu).slideUp('fast');
			var gubun = 'close';
		}

		$.post(tb_admin_url+"/ajax.menu.php",{ type: menu, gubun: gubun });
    });

	// 스크롤 최하단시 탑아이콘 출력
	$(window).scroll(function() {
		if($(window).height() < $(document).height()) {
			if($(window).scrollTop() >= 1) {
				$("#anc_header").slideDown(150);
			} else {
				$("#anc_header").slideUp(100);
			}
		}
	});


	// 사이드뷰
    var sv_hide = false;
    $(".sv_member, .sv_guest").click(function() {
        $(".sv").removeClass("sv_on");
        $(this).closest(".sv_wrap").find(".sv").addClass("sv_on");
    });

    $(".sv, .sv_wrap").hover(
        function() {
            sv_hide = false;
        },
        function() {
            sv_hide = true;
        }
    );

    $(".sv_member, .sv_guest").focusin(function() {
        sv_hide = false;
        $(".sv").removeClass("sv_on");
        $(this).closest(".sv_wrap").find(".sv").addClass("sv_on");
    });

    $(".sv a").focusin(function() {
        sv_hide = false;
    });

    $(".sv a").focusout(function() {
        sv_hide = true;
    });

    // 셀렉트 ul
    var sel_hide = false;
    $('.sel_btn').click(function() {
        $('.sel_ul').removeClass('sel_on');
        $(this).siblings('.sel_ul').addClass('sel_on');
    });

    $(".sel_wrap").hover(
        function() {
            sel_hide = false;
        },
        function() {
            sel_hide = true;
        }
    );

    $('.sel_a').focusin(function() {
        sel_hide = false;
    });

    $('.sel_a').focusout(function() {
        sel_hide = true;
    });

    $(document).click(function() {
        if(sv_hide) { // 사이드뷰 해제
            $(".sv").removeClass("sv_on");
        }
        if (sel_hide) { // 셀렉트 ul 해제
            $('.sel_ul').removeClass('sel_on');
        }
    });

    $(document).focusin(function() {
        if(sv_hide) { // 사이드뷰 해제
            $(".sv").removeClass("sv_on");
        }
        if (sel_hide) { // 셀렉트 ul 해제
            $('.sel_ul').removeClass('sel_on');
        }
    });
});