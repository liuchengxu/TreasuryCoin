package scorex.core.transaction.state


import scorex.core.NodeViewComponent
import scorex.core.block.StateChanges
import scorex.core.transaction.box.Box
import scorex.core.transaction.box.proposition.Proposition
import scorex.core.transaction.{Transaction, TransactionChanges}

import scala.util.Try

/**
  * Abstract functional interface of state which is a result of a sequential blocks applying
  */
trait MinimalState[P <: Proposition, TX <: Transaction[P, TX]] extends NodeViewComponent {
  def version: Int

  def isValid(tx: TX): Boolean = tx.validate(this).isSuccess

  def areValid(txs: Seq[TX]): Boolean = txs.forall(isValid)

  def filterValid(txs: Seq[TX]): Seq[TX] = txs.filter(isValid)

  def closedBox(boxId: Array[Byte]): Option[Box[P]]

  def accountBox(p: P): Option[Box[P]]

  def applyChanges(change: StateChanges[P]): Try[MinimalState[P, TX]]

  def rollbackTo(version: Int): Try[MinimalState[P, TX]]
}