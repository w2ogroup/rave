/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.model;

import org.apache.rave.persistence.BasicEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * A preference for a region widget.
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "region_widget_preference")
@XmlRootElement
public class RegionWidgetPreference implements BasicEntity, Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "regionWidgetPreferenceIdGenerator")
    @TableGenerator(name = "regionWidgetPreferenceIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "region_widget_preference", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "region_widget_id")
    private Long regionWidgetId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "value")
    private String value;

    public RegionWidgetPreference() {
    }

    public RegionWidgetPreference(Long entityId, Long regionWidgetId, String name, String value) {
        this.entityId = entityId;
        this.regionWidgetId = regionWidgetId;
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the persistence unique identifier
     *
     * @return id The ID of persisted object; null if not persisted
     */
    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * Gets the ID of the RegionWidget this preference is for
     * @return The ID of the RegionWidget this preference is for
     */
    public Long getRegionWidgetId() {
        return regionWidgetId;
    }

    public void setRegionWidgetId(Long regionWidgetId) {
        this.regionWidgetId = regionWidgetId;
    }

    /**
     * Gets the name of the preference
     *
     * @return The name of the preference
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of this preference
     *
     * @return The value of this preference
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegionWidgetPreference other = (RegionWidgetPreference) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "RegionWidgetPreference{" +
                "entityId=" + entityId +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}