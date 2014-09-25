package com.example.mortar.mortarflow;

import android.content.Context;
import com.example.flow.appflow.Screen;
import com.example.flow.appflow.ScreenContextFactory;
import com.example.mortar.mortarscreen.MortarScreen;
import com.example.mortar.mortarscreen.ScreenScoper;
import java.util.LinkedHashMap;
import java.util.Map;

import static mortar.Mortar.getScope;

public final class MortarContextFactory implements ScreenContextFactory {
  private final ScreenScoper screenScoper = new ScreenScoper();

  private final Map<Context, Context> contextToParent = new LinkedHashMap<>();

  public MortarContextFactory() {
  }

  @Override public Context createContext(Screen screen, Context parentContext) {
    Context newContext = screenScoper.getScreenScope(parentContext, (MortarScreen) screen)
        .createContext(parentContext);
    contextToParent.put(newContext, parentContext);
    return newContext;
  }

  @Override public void destroyContext(Context context) {
    Context parentContext = contextToParent.remove(context);
    if (parentContext == null) throw new IllegalArgumentException("Did not create " + context);
    getScope(parentContext).destroyChild(getScope(context));
  }
}
