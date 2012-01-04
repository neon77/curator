/*
 *
 *  Copyright 2011 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.curator.framework.api.transaction;

import com.netflix.curator.framework.api.ACLPathAndBytesable;
import com.netflix.curator.framework.api.CreateModable;
import com.netflix.curator.framework.api.PathAndBytesable;

public interface TransactionCreateBuilder extends
    PathAndBytesable<CuratorTransaction>,
    CreateModable<ACLPathAndBytesable<CuratorTransaction>>,
    ACLPathAndBytesable<CuratorTransaction>
{
    /**
     * <p>
     *     Hat-tip to https://github.com/sbridges for pointing this out
     * </p>
     *
     * <p>
     *     It turns out there is an edge case that exists when creating sequential-ephemeral
     *     nodes. The creation can succeed on the server, but the server can crash before
     *     the created node name is returned to the client. However, the ZK session is still
     *     valid so the ephemeral node is not deleted. Thus, there is no way for the client to
     *     determine what node was created for them.
     * </p>
     *
     * <p>
     *     Putting the create builder into protected-ephemeral-sequential mode works around this.
     *     The name of the node that is created is prefixed with a GUID. If node creation fails
     *     the normal retry mechanism will occur. On the retry, the parent path is first searched
     *     for a node that has the GUID in it. If that node is found, it is assumed to be the lost
     *     node that was successfully created on the first try and is returned to the caller.
     * </p>
     *
     * @return this
     */
    public ACLPathAndBytesable<CuratorTransaction>              withProtectedEphemeralSequential();
}