package ResturantBackend.Dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import ResturantBackend.Entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String name;
    private String role;
    private String contactNo;
    private String address;
    private String imageName;
    private MultipartFile image;
    private String password;
    private boolean fakeDelete;

    private Date createdDate;

    private Date lastModifiedDate;

    private List<Orders> orders;
}
