# shopme
# project lombok
maven: lombok
, Plugin: Lombok 

Đến đây có thể bạn sẽ thắc mắc tại sao đã thêm Lombok vào project rồi mà còn phải cài thêm Lombok Plugin vào IDE nữa???
Ví dụ bạn muốn sử dụng Lombok để generate Get/Set thì nó sẽ tự động thêm code vào class đó trước khi thành file .jar. Nhưng các IDE thì chỉ nhìn thấy các dòng code hiện tại của bạn và tham chiếu tới nó, điều này sẽ dẫn đến những thông báo lỗi khi bạn sử dụng hàm Get/Set này.
Nên để IDE hiểu rằng các class đã có các hàm Get/Set rồi, thì bạn phải cài thêm Lombok Plugin.


#JSON infinity loop
