<script>

    document.addEventListener("DOMContentLoaded", function() {
        jQuery(function($) {
            var isEmailValid = false;
            var isAuthNumberValid = false;
            var isBirthdateValid = false;
            var isPasswordValid = false;
            var isConfirmPasswordValid = false;

            function validateForm() {
                if (isEmailValid && isAuthNumberValid && isBirthdateValid && isPasswordValid && isConfirmPasswordValid) {
                    $('#signup-button').prop('disabled', false);
                } else {
                    $('#signup-button').prop('disabled', true);
                }
            }

            // 인증 이메일 발송
            $('.auth-button').on('click', function() {
                var email = $('#email').val();
                var emailDto = { email: email };
                $.ajax({
                    url: '/mailSend',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(emailDto),
                    dataType: 'json',
                    success: function(response) {
                        alert('인증 이메일이 발송되었습니다.');
                        console.log(response);
                    },
                    error: function(error) {
                        alert('인증 이메일 발송에 실패했습니다.');
                        console.error(error);
                    }
                });
            });

            // 인증번호 확인
            $('#auth-number').on('input', function() {
                var authNum = $(this).val();
                var email = $('#email').val();
                var data = JSON.stringify({ email: email, authNum: authNum });

                if (authNum.length === 6) {
                    $.ajax({
                        url: '/mailauthCheck',
                        type: 'POST',
                        contentType: 'application/json',
                        data: data,
                        success: function(response) {
                            $('#auth-number-error-message').text("인증에 성공하였습니다.");
                            $('#auth-number').prop('disabled', true);
                            isAuthNumberValid = true;
                            validateForm();
                        },
                        error: function(response) {
                            $('#auth-number-error-message').text("인증에 실패하였습니다.");
                            isAuthNumberValid = false;
                            validateForm();
                        }
                    });
                } else {
                    $('#auth-number-error-message').text("");
                    isAuthNumberValid = false;
                    validateForm();
                }
            });

            // 이메일 유효성 및 중복 검사
            $('#email').on('input', function() {
                var email = $(this).val();
                var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(email)) {
                    $('#email-error-message').text("이메일 형식이 올바르지 않습니다.");
                    $('.auth-button').prop('disabled', true);
                    isEmailValid = false;
                    validateForm();
                } else {
                    $('#email-error-message').text("");
                    var data = JSON.stringify({ email: email });
                    $.ajax({
                        url: '/mailDuplicationValidation',
                        type: 'POST',
                        contentType: 'application/json',
                        data: data,
                        success: function(response) {
                            $('#email-error-message').text("사용 가능한 이메일입니다.");
                            $('.auth-button').prop('disabled', false);
                            isEmailValid = true;
                            validateForm();
                        },
                        error: function(response) {
                            $('#email-error-message').text("이미 등록된 이메일입니다.");
                            $('.auth-button').prop('disabled', true);
                            isEmailValid = false;
                            validateForm();
                        }
                    });
                }
            });

            // 비밀번호 유효성 검사
            $('#password').on('input', function() {
                var password = $(this).val();
                var passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,20}$/;
                if (!passwordRegex.test(password)) {
                    $('#password-error-message').text("비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자리여야 합니다.");
                    isPasswordValid = false;
                } else {
                    $('#password-error-message').text("");
                    isPasswordValid = true;
                }
                validateForm();
            });

            // 비밀번호 확인
            $('#confirm-password').on('input', function() {
                var password = $('#password').val();
                var confirmPassword = $(this).val();
                if (password !== confirmPassword) {
                    $('#confirm-password-error-message').text("비밀번호가 일치하지 않습니다.");
                    isConfirmPasswordValid = false;
                } else {
                    $('#confirm-password-error-message').text("");
                    isConfirmPasswordValid = true;
                }
                validateForm();
            });

            // 생년월일 유효성 검사
            function validateBirthdate() {
                var birthdateInput = $('#birthdate').val();
                var birthdateErrorMessage = $('#birthdate-error-message');
                var datePattern = /^\d{4}-\d{2}-\d{2}$/;
                if (!birthdateInput || !datePattern.test(birthdateInput) || isNaN(new Date(birthdateInput).getTime())) {
                    birthdateErrorMessage.text("유효한 날짜를 입력하세요. (예: 1997-03-16)");
                    isBirthdateValid = false;
                } else {
                    birthdateErrorMessage.text("");
                    isBirthdateValid = true;
                }
                validateForm();
            }

            $('#birthdate').on('input', validateBirthdate);

            $('#signupForm').on('submit', function(event) {
                event.preventDefault();

                var formData = {
                    email: $('#email').val(),
                    authNumber: $('#auth-number').val(),
                    password: $('#password').val(),
                    confirmPassword: $('#confirm-password').val(),
                    birthdate: $('#birthdate').val()
                };

                var jsonData = JSON.stringify(formData);

                $.ajax({
                    url: '/user/signup',
                    type: 'POST',
                    contentType: 'application/json',
                    data: jsonData,
                    success: function(response) {
                        alert(response);
                    },
                    error: function(error) {
                        alert(xhr.responseText);
                    }
                });
            });
        });
    });
</script>