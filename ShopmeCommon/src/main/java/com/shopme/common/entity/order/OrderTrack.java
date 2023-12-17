package com.shopme.common.entity.order;

import com.shopme.common.IdBasedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "order_track")
public class OrderTrack extends IdBasedEntity {
    private String notes;
    @Column(nullable = false)
    private Date updatedTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "status",length = 45,nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Transient
    public String getUpdatedTimeOnForm() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        return dateFormat.format(this.updatedTime);
    }

    public void setUpdatedTimeOnForm(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            this.updatedTime = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
