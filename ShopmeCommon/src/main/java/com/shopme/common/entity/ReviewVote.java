package com.shopme.common.entity;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "reviews_votes")
public class ReviewVote extends IdBasedEntity {
    private static final int VOTE_UP_POINT = 1;
    private static final int VOTE_DOWN_POINT = -1;

    private int votes;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "reivew_id")
    private Review review;

    public void voteUp() {
        this.votes = VOTE_UP_POINT;
    }
    public void voteDown() {
        this.votes = VOTE_DOWN_POINT;
    }

    @Transient
    public boolean isUpVoted() {
        return this.votes == VOTE_UP_POINT;
    }

    @Transient
    public boolean isDownVoted() {
        return this.votes == VOTE_DOWN_POINT;
    }
}
