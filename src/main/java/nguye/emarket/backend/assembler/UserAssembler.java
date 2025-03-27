package nguye.emarket.backend.assembler;

import nguye.emarket.backend.controller.UserController;
import nguye.emarket.backend.entity.UserEntity;
import nguye.emarket.backend.model.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler extends
        RepresentationModelAssemblerSupport<UserEntity, User> {

    public UserAssembler() {
        super(UserController.class, User.class);
    }

    @Override
    public User toModel(UserEntity entity) {
        User user = new User(entity.getId(), entity.getUsername());

        if (entity.getImageUrl() != null) {
            user.setImageUrl(entity.getImageUrl());
        }

        return user
                .add(linkTo(methodOn(UserController.class).getUser(entity.getUsername())).withSelfRel())
                .add(linkTo(methodOn(UserController.class).getDetails(entity.getUsername())).withRel("details"));
    }

    public List<User> toListModel(Iterable<UserEntity> entities) {
        if (Objects.isNull(entities)) {
            return List.of();
        }
        return StreamSupport.stream(entities.spliterator(), false).map(this::toModel)
                .collect(toList());
    }
}
