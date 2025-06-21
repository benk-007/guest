package com.smsmode.guest.dao.specification;

import com.smsmode.guest.embeddable.ContactEmbeddable_;
import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.GuestModel_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

public class GuestSpecification {
    public static Specification<GuestModel> withIdEqual(String guestId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(GuestModel_.id), guestId);
    }

    public static Specification<GuestModel> withFirstNameLike(String firstName) {
        return (root, query, criteriaBuilder) ->
                ObjectUtils.isEmpty(firstName) ? criteriaBuilder.conjunction() : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(GuestModel_.firstName)),
                        "%" + firstName.toLowerCase() + "%"
                );
    }

    public static Specification<GuestModel> withLastNameLike(String lastName) {
        return (root, query, criteriaBuilder) ->
                ObjectUtils.isEmpty(lastName) ? criteriaBuilder.conjunction() : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(GuestModel_.lastName)),
                        "%" + lastName.toLowerCase() + "%"
                );
    }

    public static Specification<GuestModel> withEmailLike(String email) {
        return (root, query, criteriaBuilder) ->
                ObjectUtils.isEmpty(email) ? criteriaBuilder.conjunction() : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(GuestModel_.contact).get(ContactEmbeddable_.email)),
                        "%" + email.toLowerCase() + "%"
                );
    }

    public static Specification<GuestModel> withEmailEqual(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(GuestModel_.contact).get("email"), email);
    }

}