/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package MySpringMVC.V2.context;

import MySpringMVC.V2.context.support.AbstractApplicationContext;

/**
 * Interface that encapsulates event publication functionality.
 * Serves as super-interface for {@link AbstractApplicationContext}.
 *
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @see AbstractApplicationContext
 * @see ApplicationEventPublisherAware
 * @see ApplicationEvent
 * @since 1.1.1
 */
@FunctionalInterface
public interface ApplicationEventPublisher {

    /**
     * Notify all <strong>matching</strong> listeners registered with this
     * application of an application event. Events may be framework events
     * (such as RequestHandledEvent) or application-specific events.
     *
     * @param event the event to publish
     */
    void publishEvent(ApplicationEvent event);

}
