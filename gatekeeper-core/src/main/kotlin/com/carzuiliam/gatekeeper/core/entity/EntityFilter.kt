package com.carzuiliam.gatekeeper.core.entity

import com.carzuiliam.gatekeeper.core.enumerable.AttributeOperatorType

class EntityFilter(val entityAttribute: EntityAttribute, val attributeOperatorType: AttributeOperatorType, val value: Any?)