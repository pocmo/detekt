package io.gitlab.arturbosch.detekt.rules.style

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.hasCommentInside
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtWhenExpression

class OptionalWhenBraces(config: Config = Config.empty) : Rule(config) {

	override val issue: Issue = Issue(javaClass.simpleName, Severity.Style,
			"Optional braces in when expression", Debt.FIVE_MINS)

	override fun visitWhenExpression(expression: KtWhenExpression) {
		for (entry in expression.entries) {
			val blockExpression = entry.expression as? KtBlockExpression
			if (hasOneStatement(blockExpression) && hasOptionalBrace(blockExpression)) {
				report(CodeSmell(issue, Entity.from(entry), message = ""))
			}
		}
	}

	private fun hasOneStatement(blockExpression: KtBlockExpression?) =
			blockExpression?.statements?.size == 1 && !blockExpression.hasCommentInside()

	private fun hasOptionalBrace(blockExpression: KtBlockExpression?) =
			blockExpression != null && blockExpression.lBrace != null && blockExpression.rBrace != null
}
