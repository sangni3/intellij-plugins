// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package training.learn.lesson.java.navigation

import com.intellij.codeInsight.CodeInsightBundle
import com.intellij.icons.AllIcons
import com.intellij.ide.IdeBundle
import com.intellij.java.analysis.JavaAnalysisBundle
import com.intellij.openapi.wm.ToolWindowId
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.impl.content.BaseLabel
import com.intellij.testGuiFramework.framework.GuiTestUtil
import com.intellij.testGuiFramework.util.Key
import com.intellij.ui.InplaceButton
import com.intellij.ui.UIBundle
import training.commands.kotlin.TaskContext
import training.commands.kotlin.TaskRuntimeContext
import training.learn.LessonsBundle
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.LessonUtil
import training.learn.lesson.kimpl.closeAllFindTabs

class JavaInheritanceHierarchyLesson(module: Module)
  : KLesson("java.inheritance.hierarchy.lesson", LessonsBundle.message("java.inheritance.hierarchy.lesson.name"), module, "JAVA") {
  override val existedFile: String = "src/InheritanceHierarchySample.java"

  override val lessonContent: LessonContext.() -> Unit = {
    caret("foo(demo)")

    actionTask("GotoImplementation") {
      LessonsBundle.message("java.inheritance.hierarchy.goto.implementation", action(it), code("SomeInterface#foo"))
    }

    task {
      text(LessonsBundle.message("java.inheritance.hierarchy.choose.any.implementation", LessonUtil.rawEnter()))

      stateCheck {
        (virtualFile.name == "DerivedClass1.java" || virtualFile.name == "DerivedClass2.java") && atDeclarationPosition()
      }

      test {
        Thread.sleep(1000)
        GuiTestUtil.shortcut(Key.ENTER)
      }
    }

    task("GotoSuperMethod") {
      text(LessonsBundle.message("java.inheritance.hierarchy.navigate.to.base", action(it), icon(AllIcons.Gutter.ImplementingMethod)))
      stateCheck {
        virtualFile.name == "SomeInterface.java" && atDeclarationPosition()
      }

      test { actions(it) }
    }

    task("GotoImplementation") {
      text(LessonsBundle.message("java.inheritance.hierarchy.invoke.implementations.again", icon(AllIcons.Gutter.ImplementedMethod),
                                 action(it)))
      triggerByUiComponentAndHighlight { ui: InplaceButton ->
        ui.toolTipText == IdeBundle.message("show.in.find.window.button.name")
      }

      test { actions(it) }
    }

    task {
      before {
        closeAllFindTabs()
      }
      text(LessonsBundle.message("java.inheritance.hierarchy.open.in.find.tool.window", findToolWindow(),
                                 icon(ToolWindowManager.getInstance(project).getLocationIcon(ToolWindowId.FIND, AllIcons.General.Pin_tab))))
      triggerByUiComponentAndHighlight(highlightBorder = false, highlightInside = false) { ui: BaseLabel ->
        ui.text == (CodeInsightBundle.message("goto.implementation.findUsages.title", "foo")) ||
        ui.text == (JavaAnalysisBundle.message("navigate.to.overridden.methods.title", "foo"))
      }
      test {
        ideFrame {
          val target = previous.ui!!
          jComponent(target).click()
          jComponent(target).click() // for some magic reason one click sometimes doesn't work :(
        }
      }
    }

    actionTask("HideActiveWindow") {
      LessonsBundle.message("java.inheritance.hierarchy.hide.find.tool.window", action(it), findToolWindow())
    }

    actionTask("MethodHierarchy") {
      LessonsBundle.message("java.inheritance.hierarchy.open.method.hierarchy", action(it))
    }

    actionTask("HideActiveWindow") {
      LessonsBundle.message("java.inheritance.hierarchy.hide.method.hierarchy", hierarchyToolWindow(), action(it))
    }

    actionTask("TypeHierarchy") {
      LessonsBundle.message("java.inheritance.hierarchy.open.class.hierarchy", action(it))
    }

    task {
      text(LessonsBundle.message("java.inheritance.hierarchy.last.note",
                                 action("GotoImplementation"),
                                 action("GotoSuperMethod"),
                                 action("MethodHierarchy"),
                                 action("TypeHierarchy"),
                                 action("GotoAction"),
                                 strong("hierarchy")))
    }
  }

  private fun TaskRuntimeContext.atDeclarationPosition(): Boolean {
    return editor.document.charsSequence.let {
      it.subSequence(editor.caretModel.currentCaret.offset, it.length).startsWith("foo(FileStructureDemo demo)")
    }
  }

  private fun TaskContext.findToolWindow() = strong(UIBundle.message("tool.window.name.find"))
  private fun TaskContext.hierarchyToolWindow() = strong(UIBundle.message("tool.window.name.hierarchy"))
}
