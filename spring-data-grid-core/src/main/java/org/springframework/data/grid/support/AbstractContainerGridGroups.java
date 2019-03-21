/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.grid.support;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.grid.ContainerGridGroups;
import org.springframework.data.grid.ContainerGroup;
import org.springframework.data.grid.ContainerNode;
import org.springframework.data.grid.listener.ContainerGridGroupsListener;
import org.springframework.data.grid.listener.DefaultContainerGridGroupsListener;
import org.springframework.util.Assert;

/**
 * Simple {@code ContainerGridGroups} base implementation keeping {@code ContainerGroup}s
 * in a {@code ConcurrentHashMap}.
 *
 * @author Janne Valkealahti
 *
 * @param <GID> the type of {@link ContainerGroup} identifier
 * @param <NID> the type of {@link ContainerNode} identifier
 * @param <CN> the type of {@link ContainerNode}
 * @param <CG> the type of {@link ContainerGroup}
 */
public abstract class AbstractContainerGridGroups<GID, NID, CN extends ContainerNode<NID>, CG extends ContainerGroup<GID, NID, CN>>
		extends AbstractContainerGrid<NID, CN> implements ContainerGridGroups<GID, NID, CN, CG> {

	private ConcurrentHashMap<GID, CG> groups = new ConcurrentHashMap<GID, CG>();

	/** Listener dispatcher for container group events */
	private DefaultContainerGridGroupsListener<GID, NID, CN, CG> groupsListeners =
			new DefaultContainerGridGroupsListener<GID, NID, CN, CG>();

	@Override
	public boolean addGroup(CG group) {
		Assert.notNull(group, "Node must not be null");
		if (groups.putIfAbsent(group.getId(), group) == null) {
			notifyGroupAdded(group);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeGroup(GID id) {
		Assert.notNull(id, "Group identifier must not be null");
		CG removed = groups.remove(id);
		if (removed != null) {
			notifyGroupRemoved(removed);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public CG getGroup(GID id) {
		return groups.get(id);
	}

	@Override
	public Collection<CG> getGroups() {
		return groups.values();
	}

	@Override
	public CG getGroupByNode(NID id) {
		for (CG group : groups.values()) {
			if (group.hasNode(id)) {
				return group;
			}
		}
		return null;
	}

	@Override
	public void addContainerGridGroupsListener(ContainerGridGroupsListener<GID, NID, CN, CG> listener) {
		groupsListeners.register(listener);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerGroup} has been added to a {@code ContainerGridGroups}.
	 *
	 * @param group the group
	 */
	protected void notifyGroupAdded(CG group) {
		groupsListeners.groupAdded(group);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerGroup} has been removed from a {@code ContainerGridGroups}.
	 *
	 * @param group the group
	 */
	protected void notifyGroupRemoved(CG group) {
		groupsListeners.groupRemoved(group);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerNode} has been added to a {@code ContainerGroup}.
	 *
	 * @param group the group
	 * @param node the node
	 */
	protected void notifyNodeAdded(CG group, CN node) {
		groupsListeners.nodeAdded(group, node);
	}

	/**
	 * Notifies registered {@code ContainerGridGroupsListener}s that
	 * a {@code ContainerNode} has been removed from a {@code ContainerGroup}.
	 *
	 * @param group the group
	 * @param node the node
	 */
	protected void notifyNodeRemoved(CG group, CN node) {
		groupsListeners.nodeRemoved(group, node);
	}

}
