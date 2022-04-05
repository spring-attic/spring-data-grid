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

import org.springframework.data.grid.ContainerGrid;
import org.springframework.data.grid.ContainerNode;
import org.springframework.data.grid.listener.ContainerGridListener;
import org.springframework.data.grid.listener.DefaultContainerGridListener;
import org.springframework.util.Assert;

/**
 * Simple {@code ContainerGrid} base implementation keeping {@code ContainerNode}s
 * in a {@code ConcurrentHashMap}.
 *
 * @author Janne Valkealahti
 *
 * @param <NID> the type of {@link ContainerNode} identifier
 * @param <CN> the type of {@link ContainerNode}
 */
public abstract class AbstractContainerGrid<NID, CN extends ContainerNode<NID>> implements ContainerGrid<NID, CN> {

	/** Listener dispatcher for container grid events */
	private DefaultContainerGridListener<NID, CN> gridListeners = new DefaultContainerGridListener<NID, CN>();

	private ConcurrentHashMap<NID, CN> nodes = new ConcurrentHashMap<NID, CN>();

	@Override
	public Collection<CN> getNodes() {
		return nodes.values();
	}

	@Override
	public CN getNode(NID id) {
		return nodes.get(id);
	}

	@Override
	public boolean addNode(CN node) {
		Assert.notNull(node, "Node must not be null");
		if (nodes.putIfAbsent(node.getId(), node) == null) {
			notifyNodeAdded(node);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeNode(NID id) {
		Assert.notNull(id, "Node identifier must not be null");
		CN removed = nodes.remove(id);
		if (removed != null) {
			notifyNodeRemoved(removed);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addContainerGridListener(ContainerGridListener<NID, CN> listener) {
		gridListeners.register(listener);
	}

	/**
	 * Notifies registered {@code ContainerGridListener}s that
	 * a {@code ContainerNode} has been added to a {@code ContainerGrid}.
	 *
	 * @param node the node
	 */
	protected void notifyNodeAdded(CN node) {
		gridListeners.nodeAdded(node);
	}

	/**
	 * Notifies registered {@code ContainerGridListener}s that
	 * a {@code ContainerNode} has been removed from a {@code ContainerGrid}.
	 *
	 * @param node the node
	 */
	protected void notifyNodeRemoved(CN node) {
		gridListeners.nodeRemoved(node);
	}

}
