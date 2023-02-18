package com.example.piyachok.services;

import com.example.piyachok.dao.PlaceDAO;
import com.example.piyachok.dao.TypeDAO;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.News;
import com.example.piyachok.models.Place;
import com.example.piyachok.models.Type;
import com.example.piyachok.models.User;
import com.example.piyachok.models.dto.ItemListDTO;
import com.example.piyachok.models.dto.PlaceDTO;
import com.example.piyachok.models.dto.UserDTO;
import com.example.piyachok.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {


    private UserDAO userDAO;
    private PlaceDAO placeDAO;

    private ItemListService itemListService;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;
    private JwtUtils jwtUtils;

    private TypeDAO typeDAO;


    public static UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setCreationDate(user.getCreationDate());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setActivated(user.isActivated());
        userDTO.setBlocked(user.isBlocked());
        userDTO.setPlaces(user.getPlaces());
        userDTO.setNews(user.getNews());
        userDTO.setPhoto(user.getPhoto());

        return userDTO;
    }

    public ResponseEntity<UserDTO> saveUser(UserDTO userDTO) {
        if (userDTO != null) {
            User user = new User();
            user.setLogin(userDTO.getLogin());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());
            userDAO.save(user);
            if (user.getId() != 0) {
                mailService.sendMail(user);
            }
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<HttpStatus> deleteUserById(int id) {
        User user = userDAO.findById(id).orElse(new User());

        if (user.getLogin() != null) {
            for (Place place : user.getPlaces()) {
                List<Type> allByPlace = typeDAO.findAllByPlace(place);
                for (Type type : allByPlace) {
                    type.getPlace().remove(place);
                    typeDAO.save(type);
                }
            }
            user.setFavoritePlaces(new ArrayList<>());
            userDAO.deleteById(id);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ItemListDTO<UserDTO>> findAllUsers(Integer page) {
        boolean old = false;
        int itemsOnPage = 10;
        List<UserDTO> users = userDAO.findAll()
                .stream()
                .map(UserService::convertUserToUserDTO)
                .collect(Collectors.toList());
        if (users.size() != 0) {
            return itemListService.createResponseEntity(users, itemsOnPage, page, old);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<UserDTO> findUserByID(int userId) {
        User user = userDAO.findById(userId).orElse(new User());
        if (user.getLogin() != null) {
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<UserDTO> updateUserById(int id, User user) {
        User oldUser = userDAO.findById(id).orElse(new User());
        if (oldUser.getLogin() != null) {
            if (!user.getPassword().equals("")) {
                oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());
            oldUser.setBirthDate(user.getBirthDate());
            oldUser.setEmail(user.getEmail());
            oldUser.setRole(user.getRole());
            oldUser.setActivated(user.isActivated());
            oldUser.setBlocked(user.isBlocked());


            userDAO.save(oldUser);
            return new ResponseEntity<>(convertUserToUserDTO(oldUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public boolean addNewsToUser(String login, News news) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        if (user.getLogin() != null) {
            user.getNews().add(news);
            news.setUser(user);
            return true;
        }
        return false;
    }

    public ResponseEntity<ItemListDTO<PlaceDTO>> getFavoritePlacesByUserLogin(String login, Integer page) {
        boolean old = false;
        int itemsOnPage = 10;
        User user = userDAO.findUserByLogin(login).orElse(new User());

        if (user.getLogin() != null) {
            List<PlaceDTO> placeDTOS = user.getFavoritePlaces()
                    .stream()
                    .map(PlaceService::convertPlaceToPlaceDTO).collect(Collectors.toList());
            return itemListService.createResponseEntity(placeDTOS, itemsOnPage, page, old);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<UserDTO> addPlaceToFavoriteByPlaceIdAndUserLogin(int placeId, String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        Place place = placeDAO.findById(placeId).orElse(new Place());
        if (user.getFavoritePlaces()
                .stream()
                .anyMatch(favoritePlace -> favoritePlace == place)) {
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        if (user.getLogin() != null && place.getName() != null) {
            user.getFavoritePlaces().add(place);
            userDAO.save(user);


            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Boolean> checkPlaceIsFavoriteByPlaceIdAndUserLogin(int placeId, String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        Place place = placeDAO.findById(placeId).orElse(new Place());
        if (user.getLogin() == null || place.getName() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (user.getFavoritePlaces()
                .stream()
                .anyMatch(favoritePlace -> favoritePlace == place)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    public ResponseEntity<UserDTO> deletePlaceFromFavoriteByPlaceIdUserLogin(int placeId, String login) {
        User user = userDAO.findUserByLogin(login).orElse(new User());
        Place place = placeDAO.findById(placeId).orElse(new Place());
        if (user.getLogin() == null || place.getName() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.getFavoritePlaces().remove(place)) {
            userDAO.save(user);
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<UserDTO> activateUser(String activateToken) {
        String login = jwtUtils.getUserLoginFromJwtToken(activateToken);
        User user = userDAO.findUserByLogin(login).orElse(new User());
        if (user.getLogin() != null) {
            user.setActivated(true);
            userDAO.save(user);
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<HttpStatus> sendResetPasswordToken(String userLogin) {
        User user = userDAO.findUserByLogin(userLogin).orElse(new User());
        if (user.getLogin() != null) {
            user.setResetPasswordToken(generateRandomPassword(10));
            user.setResetPasswordTokenExpiryDate(System.currentTimeMillis() + 1800000);
            userDAO.save(user);
            mailService.sendResetPasswordTokenMail(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<UserDTO> resetPasswordAndSetNew(String userLogin, String password, String resetPasswordToken) {
        User user = userDAO.findUserByLogin(userLogin).orElse(new User());
        if (user.getLogin() != null
                && resetPasswordToken.equals(user.getResetPasswordToken())
                && user.getResetPasswordTokenExpiryDate() > System.currentTimeMillis()) {
            user.setPassword(passwordEncoder.encode(password));
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiryDate(0);
            userDAO.save(user);
            return new ResponseEntity<>(convertUserToUserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            stringBuilder.append(chars.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    public ResponseEntity<UserDTO> addPhotoToUserByLogin(@NonNull String login,@NonNull MultipartFile photo) {

        User user=userDAO.findUserByLogin(login).orElse(new User());
        if (user.getLogin()!=null){
            String originalFilename = photo.getOriginalFilename();
            user.setPhoto(originalFilename);
            userDAO.save(user);
            File usersPhoto = new File("src"+ File.separator+"main"+ File.separator+"resources"+File.separator+"usersPhoto");
            if (!usersPhoto.exists()){
                usersPhoto.mkdir();
            }
            String pathToSaveFile=usersPhoto.getAbsolutePath()+File.separator+originalFilename;
            try {
                photo.transferTo(new File(pathToSaveFile));
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(convertUserToUserDTO(user),HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
