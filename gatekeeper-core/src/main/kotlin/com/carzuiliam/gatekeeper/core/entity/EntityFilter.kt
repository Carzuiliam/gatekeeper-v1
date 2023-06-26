package com.carzuiliam.gatekeeper.core.entity

import com.carzuiliam.gatekeeper.core.enumerable.OperatorType

class EntityFilter(val entityAttribute: EntityAttribute, val operatorType: OperatorType, val value: Any?)