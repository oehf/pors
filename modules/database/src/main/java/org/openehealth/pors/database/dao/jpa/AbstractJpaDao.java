/*
* Copyright 2012 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.openehealth.pors.database.dao.jpa;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

/**
 * @author jr
 *
 */
public abstract class AbstractJpaDao<I, E>
{
	protected Class<E> entityClass;
	protected EntityManager em;
	
	@SuppressWarnings("unchecked")
	protected AbstractJpaDao(EntityManager manager)
	{
		this.em = manager;
		ParameterizedType genericSuperclass = (ParameterizedType) this.
			getClass().getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass.
			getActualTypeArguments()[1];
	}
	
	public void insert(E entity)
	{
		this.em.persist(entity);
	}
	
	public void delete(E entity)
	{
		this.em.remove(entity);
	}
	
	public void update(E entity)
	{
		this.em.merge(entity);
	}
	
	public void refresh(E entity)
	{
		this.em.refresh(entity);
	}
	
	public E getById(I id)
	{
		try
		{
			return this.em.find(this.entityClass, id);
		}
		catch (EntityNotFoundException e)
		{
			return null;
		}
	}
	
	public E getReferenceById(I id)
	{
		try
		{
			return this.em.getReference(this.entityClass, id);
		}
		catch (EntityNotFoundException e)
		{
			return null;
		}
	}
}
