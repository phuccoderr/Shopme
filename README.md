# Shop Artermis
Web bán laptop được xây dựng trên framework spring boot 3.1.5
# Thiết lập các cài đặt môi trường
 - IDE: Intellij Idea
 - JDK: 17.0.9 https://download.oracle.com/java/17/archive/jdk-17.0.9_windows-x64_bin.zip
 - Maven 3.9.3 [apache-maven-3.9.6-bin.zip](https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip)
 - Enviroment setup: https://www.tutorialspoint.com/maven/maven_environment_setup.htm
 - Java and html,css,javascript
# Database Mysql
 - community version 8.0.33
# Library và Framework
 - Framework: bootstrap (css)
 - Library: jquery
 - Spring framework:
 	+ spring-boot-devtools
	+ spring-boot-starter-thymeleaf
   	+ spring-boot-starter-data-jpa ( ánh xạ đối tượng java với cơ sở dữ liệu )
   	+ spring-boot-starter-test ( kiểm thử ứng dụng )
   	+ spring-boot-starter-security ( bảo mật )
   	+ spring-boot-starter-mail ( gửi mail )
   	+ spring-boot-starter-oauth2-client ( chứng thực đăng nhập và lấy tài nguyên của một service bên thứ 3, đăng nhập facebook,github,google )
 - Thymleaf extras
   	+ thymeleaf-extras-springsecurity6
 - assertj
   	+ assertj-core ( đơn giản hóa viết assertions )
 - com.mysql
   	+ mysql-connector-j
 - webjars
    + bootstrap ( version 5.3.2 )
    + jquery ( 3.4.1 )
 - projectlombok
    + lombok ( tự sinh ra các hàm setter/getter, hàm khởi tạo, toString... )
    lưu ý: phải cài thêm plugin cho IDE
 - software.amazon.awssdk
    + s3 ( hỗ trợ phát triển trên nền tảng Amazon )
 - com.fasterxml.jackson.core
    + jackson-databind ( fix JSON infinite loop khi call API )
    Bạn cũng có thể tìm hiểu DTO nếu không muốn sử dụng thư viện này!
 - net.bytebuddy
    + byte-buddy ( chỉ để random String, thường dùng để xác thực tài khoản hoặc quên mật khẩu )
 - com.github.librepdf
    + openpdf ( tạo pdf )
# Các file ngoài để hỗ trợ giao diện
 - fontawesome: https://fontawesome.com ( lấy các icon )
 - jquery-number: https://github.com/customd/jquery-number ( plugin định dạng số )
 - theme krajee rating star: https://plugins.krajee.com/star-rating-demo-theme-uni ( plugin đánh giá sao từ khách hàng )
 - google chart: https://developers.google.com/chart/interactive/docs/quick_start ( để lấy đồ thị tính số doanh thu ) 
# Amazon CLoud ( Enviroment setup AWS Cloud )
 - AWS_ACCESS_KEY_ID: ****
 - AWS_BUCKET_NAME: ****
 - AWS_REGION: ****
 - AWS_SECRET_ACCESS_KEY: ****
