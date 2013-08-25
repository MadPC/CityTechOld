package com.madpcgaming.mt.handlers;

import com.madpcgaming.mt.helpers.LocalizationHelper;
import com.madpcgaming.mt.lib.Localizations;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class LocalizationHandler
{
	
	public static void loadLanguages()
	{
		for (String localizationFile : Localizations.localeFiles)
		{
			LanguageRegistry.instance().loadLocalization(localizationFile, LocalizationHelper.getLocaleFromFileName(localizationFile),
					LocalizationHelper.isXMLLanguageFile(localizationFile));
		}
	}
	
}
