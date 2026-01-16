package com.ts2.centr.repo;

import com.ts2.centr.models.Cart;
import com.ts2.centr.models.CartStatus;
import com.ts2.centr.security.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {

    Optional<Cart> findByUserAndStatus (User user, CartStatus cartStatus);

    Optional<Cart> findByUserIsNullAndStatus (CartStatus cartStatus);

}
