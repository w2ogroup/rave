/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.shindig.social.opensocial.jpa;

import org.apache.shindig.social.opensocial.jpa.api.DbObject;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

/**
 * The final storage of data in the application datamap. Values are limited here to 4K in size.
 */
@Entity
@Table(name="application_datavalue")
public class ApplicationDataMapValueDb implements DbObject {

  /**
   * The internal object ID used for references to this object
   */
  @Id
  @Column(name = "oid")
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "applicationDataMapValueIdGenerator")
  @TableGenerator(name = "applicationDataMapValueIdGenerator", table = "RAVE_SHINDIG_SEQUENCES", pkColumnName = "SEQ_NAME",
          valueColumnName = "SEQ_COUNT", pkColumnValue = "application_datavalue", allocationSize = 1, initialValue = 1)
  private long objectId;

  /**
   * An optimistic locking field.
   */
  @Version
  @Column(name = "version")
  private long version;

  /**
   * Each entry is associated with an application Data Map
   */
  @ManyToOne(targetEntity=ApplicationDataMapDb.class)
  @JoinColumn(name="application_datamap_id", referencedColumnName="oid")
  private ApplicationDataMapDb applicationDataMap;
  
  /**
   * Each entry has a name
   */
  @Basic
  @Column(name="name", length=255)
  private String name;
  
  /**
   * Each entry has a value (4K limit to size)
   */
  @Basic
  @Column(name="value", length=4094)
  private String value;

  /**
   * @return the applicationDataMap
   */
  public ApplicationDataMapDb getApplicationDataMap() {
    return applicationDataMap;
  }

  /**
   * @param applicationDataMap the applicationDataMap to set
   */
  public void setApplicationDataMap(ApplicationDataMapDb applicationDataMap) {
    this.applicationDataMap = applicationDataMap;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return the objectId
   */
  public long getObjectId() {
    return objectId;
  }

  /**
   * @return the version
   */
  public long getVersion() {
    return version;
  }
}