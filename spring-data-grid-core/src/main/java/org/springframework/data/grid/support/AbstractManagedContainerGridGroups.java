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

import org.springframework.data.grid.ContainerGroup;
import org.springframework.data.grid.ContainerNode;
import org.springframework.data.grid.GroupsRebalanceData;
import org.springframework.data.grid.ManagedContainerGridGroups;

/**
 * Simple {@code ManagedContainerGridGroups} base implementation.
 *
 * @author Janne Valkealahti
 *
 * @param <GID> the type of {@link ContainerGroup} identifier
 * @param <NID> the type of {@link ContainerNode} identifier
 * @param <CN> the type of {@link ContainerNode}
 * @param <CG> the type of {@link ContainerGroup}
 * @param <GRD> the type of {@link GroupsRebalanceData}
 */
public abstract class AbstractManagedContainerGridGroups<GID, NID, CN extends ContainerNode<NID>, CG extends ContainerGroup<GID, NID, CN>, GRD extends GroupsRebalanceData>
		extends AbstractContainerGridGroups<GID, NID, CN, CG> implements ManagedContainerGridGroups<GID, NID, CN, CG, GRD> {

	@Override
	public boolean setGroupSize(GID id, int size) {
		return false;
	}

	@Override
	public GRD getGroupsRebalanceData() {
		return null;
	}

}
