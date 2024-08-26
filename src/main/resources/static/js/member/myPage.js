function uploadImage(input) {
    if (input.files && input.files[0]) {
        var formData = new FormData();
        formData.append('img', input.files[0]);

        $.ajax({
            type: 'POST',
            url: '/api/members/profile-image',
            data: formData,
            contentType: false,
            processData: false,
            success: function(data) {
                // 이미지 업로드 성공 시 처리
                console.log('Image uploaded successfully:', data);
                $('#profileImage').attr('src', data);
            },
            error: function(xhr, status, error) {
                // 이미지 업로드 실패 시 처리
                console.error('Error uploading image:', error);
            }
        });
    } else {
        console.error('파일이 선택되지 않았습니다.');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const profileImageDiv = document.getElementById('profileImageDiv');
    const reviewImg1 = document.getElementById('reviewImg1');
    const profileImage = document.getElementById('profileImage');

    profileImageDiv.addEventListener('click', function() {
        reviewImg1.click();
    });

    reviewImg1.addEventListener('change', function() {
        uploadImage(this);
    });

    const profileImageInput = document.getElementById('profile-image-input');
    profileImageInput.onchange = uploadImage;
});


function editNickname() {
        document.getElementById('nickname').style.display = 'none';
        document.getElementById('editButton').style.display = 'none';
        document.getElementById('nicknameForm').style.display = 'inline';
        document.getElementById('newNicknameInput').value = document.getElementById('nickname').innerText;
        document.getElementById('newNicknameInput').focus();
    }

   function submitNickname() {
       var newNickname = document.getElementById('newNicknameInput').value;

       console.log('변경할 닉네임: ', newNickname); // 디버깅 메시지 추가

       // AJAX로 닉네임 변경 요청
       $.ajax({
           url: '/api/members/updateNickname',
           type: 'POST',
           contentType: 'application/json',
           data: JSON.stringify({ newNickname: newNickname }),
           success: function(response) {
               console.log('닉네임 변경 성공: ', response); // 디버깅 메시지 추가
               document.getElementById('nickname').innerText = response.name; // 수정된 부분
               document.getElementById('nickname').style.display = 'inline';
               document.getElementById('editButton').style.display = 'inline';
               document.getElementById('nicknameForm').style.display = 'none';
               document.getElementById('newNicknameInput').value = ''; // 입력 필드 초기화
           },
           error: function(jqXHR, textStatus, errorThrown) {
               console.error('닉네임 변경 실패: ', textStatus, errorThrown); // 디버깅 메시지 추가
           }
       });
   }



    // 클릭 이벤트 핸들러
    document.addEventListener("click", function(event) {
        var nicknameInput = document.getElementById('newNicknameInput');
        var nicknameForm = document.getElementById('nicknameForm');
        var editButton = document.getElementById('editButton');
        var nickname = document.getElementById('nickname');

        if (event.target !== nicknameInput && event.target !== editButton && event.target !== document.getElementById('submitButton')) {
            if (nicknameForm.style.display === 'inline') {
                nickname.style.display = 'inline';
                editButton.style.display = 'inline';
                nicknameForm.style.display = 'none';
            }
        }
    });

    // 엔터키 감지 핸들러
    document.addEventListener("keydown", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // 기본 엔터키 동작 방지
            submitNickname();
        }
    });


