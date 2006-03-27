package com.intellij.uiDesigner.designSurface;

import com.intellij.openapi.actionSystem.*;
import com.intellij.uiDesigner.actions.*;
import com.intellij.uiDesigner.propertyInspector.UIDesignerToolWindowManager;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Anton Katilin
 * @author Vladimir Kondratyev
 */
final class GlassLayer extends JComponent implements DataProvider{
  private final GuiEditor myEditor;

  public GlassLayer(final GuiEditor editor){
    myEditor = editor;
    enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);

    registerKeyboardAction(new MoveSelectionToRightAction(myEditor, false), IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT);
    registerKeyboardAction(new MoveSelectionToLeftAction(myEditor, false), IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT);
    registerKeyboardAction(new MoveSelectionToUpAction(myEditor, false), IdeActions.ACTION_EDITOR_MOVE_CARET_UP);
    registerKeyboardAction(new MoveSelectionToDownAction(myEditor, false), IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN);

    registerKeyboardAction(new MoveSelectionToRightAction(myEditor, true), "EditorRightWithSelection");
    registerKeyboardAction(new MoveSelectionToLeftAction(myEditor, true), "EditorLeftWithSelection");
    registerKeyboardAction(new MoveSelectionToUpAction(myEditor, true), "EditorUpWithSelection");
    registerKeyboardAction(new MoveSelectionToDownAction(myEditor, true), "EditorDownWithSelection");

    // F2 should start inplace editing
    final StartInplaceEditingAction startInplaceEditingAction = new StartInplaceEditingAction(editor);
    startInplaceEditingAction.registerCustomShortcutSet(
      new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0)),
      this
    );
  }

  private void registerKeyboardAction(final AnAction action, @NonNls final String actionId) {
    action.registerCustomShortcutSet(
      ActionManager.getInstance().getAction(actionId).getShortcutSet(),
      this
    );
  }

  protected void processKeyEvent(final KeyEvent e){
    myEditor.myProcessor.processKeyEvent(e);
  }

  protected void processMouseEvent(final MouseEvent e){
    if(e.getID() == MouseEvent.MOUSE_PRESSED){
      requestFocusInWindow();
    }
    myEditor.myProcessor.processMouseEvent(e);
  }

  protected void processMouseMotionEvent(final MouseEvent e){
    myEditor.myProcessor.processMouseEvent(e);
  }

  /**
   * Provides {@link DataConstants#NAVIGATABLE} to navigate to
   * binding of currently selected component (if any)
   */
  public Object getData(final String dataId) {
    if(DataConstants.NAVIGATABLE.equals(dataId)) {
      return UIDesignerToolWindowManager.getInstance(myEditor.getProject()).getComponentTree().getData(dataId);
    }
    else{
      return null;
    }
  }
}
