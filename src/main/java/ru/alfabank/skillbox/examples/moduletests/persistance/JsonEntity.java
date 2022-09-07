package ru.alfabank.skillbox.examples.moduletests.persistance;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "jsons", schema = "module_tests_schema")
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@AllArgsConstructor
public class JsonEntity {
    @Id
    private Long id;

    @Type(type = "jsonb")
    @Column(nullable = false, columnDefinition = "jsonb")
    private String json;

    public JsonEntity() {
        this.id = RandomUtils.nextLong(1L, 1000L);
    }
}
