package com.dbeqiraj.youtubedownloader.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by d.beqiraj on 4/13/2018.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerService {
}
